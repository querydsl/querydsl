/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.mongodb.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import com.mongodb.DBRef;
import com.querydsl.core.types.*;
import com.querydsl.mongodb.MongodbOps;
import org.bson.BsonJavaScript;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Serializes the given Querydsl query to a Document query for MongoDB.
 *
 * @author Mark Paluch
 */
public abstract class MongodbDocumentSerializer implements Visitor<Object, Void> {

    public Object handle(Expression<?> expression) {
        return expression.accept(this, null);
    }

    public Document toSort(List<OrderSpecifier<?>> orderBys) {
        Document sort = new Document();
        for (OrderSpecifier<?> orderBy : orderBys) {
            Object key = orderBy.getTarget().accept(this, null);
            sort.append(key.toString(), orderBy.getOrder() == Order.ASC ? 1 : -1);
        }
        return sort;
    }

    @Override
    public Object visit(Constant<?> expr, Void context) {
        if (Enum.class.isAssignableFrom(expr.getType())) {
            @SuppressWarnings("unchecked") //Guarded by previous check
                    Constant<? extends Enum<?>> expectedExpr = (Constant<? extends Enum<?>>) expr;
            return expectedExpr.getConstant().name();
        } else {
            return expr.getConstant();
        }
    }

    @Override
    public Object visit(TemplateExpression<?> expr, Void context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(FactoryExpression<?> expr, Void context) {
        throw new UnsupportedOperationException();
    }

    protected String asDBKey(Operation<?> expr, int index) {
        return (String) asDBValue(expr, index);
    }

    protected Object asDBValue(Operation<?> expr, int index) {
        return expr.getArg(index).accept(this, null);
    }

    private String regexValue(Operation<?> expr, int index) {
        return Pattern.quote(expr.getArg(index).accept(this, null).toString());
    }

    protected Document asDocument(String key, Object value) {
        return new Document(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visit(Operation<?> expr, Void context) {
        Operator op = expr.getOperator();
        if (op == Ops.EQ) {
            if (expr.getArg(0) instanceof Operation) {
                Operation<?> lhs = (Operation<?>) expr.getArg(0);
                if (lhs.getOperator() == Ops.COL_SIZE || lhs.getOperator() == Ops.ARRAY_SIZE) {
                    return asDocument(asDBKey(lhs, 0), asDocument("$size", asDBValue(expr, 1)));
                } else {
                    throw new UnsupportedOperationException("Illegal operation " + expr);
                }
            } else if (expr.getArg(0) instanceof Path) {
                Path<?> path = (Path<?>) expr.getArg(0);
                Constant<?> constant = (Constant<?>) expr.getArg(1);
                return asDocument(asDBKey(expr, 0), convert(path, constant));
            }
        } else if (op == Ops.STRING_IS_EMPTY) {
            return asDocument(asDBKey(expr, 0), "");

        } else if (op == Ops.AND) {
            Queue<Map<Object, Object>> pendingDocuments = collectConnectorArgs("$and", expr);
            List<Map<Object, Object>> unmergeableDocuments = new ArrayList<Map<Object, Object>>();
            List<Map<Object, Object>> generatedDocuments = new ArrayList<Map<Object, Object>>();

            while (!pendingDocuments.isEmpty()) {

                Map<Object, Object> lhs = pendingDocuments.poll();

                for (Map<Object, Object> rhs : pendingDocuments) {
                    Set<Object> lhs2 = new LinkedHashSet<Object>(lhs.keySet());
                    lhs2.retainAll(rhs.keySet());
                    if (lhs2.isEmpty()) {
                        lhs.putAll(rhs);
                    } else {
                        unmergeableDocuments.add(rhs);
                    }
                }

                generatedDocuments.add(lhs);
                pendingDocuments = new LinkedList<Map<Object, Object>>(unmergeableDocuments);
                unmergeableDocuments = new LinkedList<Map<Object, Object>>();
            }

            return generatedDocuments.size() == 1 ? generatedDocuments.get(0) : asDocument("$and", generatedDocuments);
        } else if (op == Ops.NOT) {
            //Handle the not's child
            Operation<?> subOperation = (Operation<?>) expr.getArg(0);
            Operator subOp = subOperation.getOperator();
            if (subOp == Ops.IN) {
                return visit(ExpressionUtils.operation(Boolean.class, Ops.NOT_IN, subOperation.getArg(0),
                        subOperation.getArg(1)), context);
            } else {
                Document arg = (Document) handle(expr.getArg(0));
                return negate(arg);
            }

        } else if (op == Ops.OR) {
            return asDocument("$or", collectConnectorArgs("$or", expr));

        } else if (op == Ops.NE) {
            Path<?> path = (Path<?>) expr.getArg(0);
            Constant<?> constant = (Constant<?>) expr.getArg(1);
            return asDocument(asDBKey(expr, 0), asDocument("$ne", convert(path, constant)));

        } else if (op == Ops.STARTS_WITH) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression("^" + regexValue(expr, 1)));

        } else if (op == Ops.STARTS_WITH_IC) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression("^" + regexValue(expr, 1), "i"));

        } else if (op == Ops.ENDS_WITH) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression(regexValue(expr, 1) + "$"));

        } else if (op == Ops.ENDS_WITH_IC) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression(regexValue(expr, 1) + "$", "i"));

        } else if (op == Ops.EQ_IGNORE_CASE) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression("^" + regexValue(expr, 1) + "$", "i"));

        } else if (op == Ops.STRING_CONTAINS) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression(".*" + regexValue(expr, 1) + ".*"));

        } else if (op == Ops.STRING_CONTAINS_IC) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression(".*" + regexValue(expr, 1) + ".*", "i"));

        } else if (op == Ops.MATCHES) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression(asDBValue(expr, 1).toString()));

        } else if (op == Ops.MATCHES_IC) {
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression(asDBValue(expr, 1).toString(), "i"));

        } else if (op == Ops.LIKE) {
            String regex = ExpressionUtils.likeToRegex((Expression) expr.getArg(1)).toString();
            return asDocument(asDBKey(expr, 0), new BsonRegularExpression(regex));

        } else if (op == Ops.BETWEEN) {
            Document value = new Document("$gte", asDBValue(expr, 1));
            value.append("$lte", asDBValue(expr, 2));
            return asDocument(asDBKey(expr, 0), value);

        } else if (op == Ops.IN) {
            int constIndex = 0;
            int exprIndex = 1;
            if (expr.getArg(1) instanceof Constant<?>) {
                constIndex = 1;
                exprIndex = 0;
            }
            if (Collection.class.isAssignableFrom(expr.getArg(constIndex).getType())) {
                @SuppressWarnings("unchecked") //guarded by previous check
                Collection<?> values = ((Constant<? extends Collection<?>>) expr.getArg(constIndex)).getConstant();
                return asDocument(asDBKey(expr, exprIndex), asDocument("$in", values));
            } else {
                Path<?> path = (Path<?>) expr.getArg(exprIndex);
                Constant<?> constant = (Constant<?>) expr.getArg(constIndex);
                return asDocument(asDBKey(expr, exprIndex), convert(path, constant));
            }

        } else if (op == Ops.NOT_IN) {
            int constIndex = 0;
            int exprIndex = 1;
            if (expr.getArg(1) instanceof Constant<?>) {
                constIndex = 1;
                exprIndex = 0;
            }
            if (Collection.class.isAssignableFrom(expr.getArg(constIndex).getType())) {
                @SuppressWarnings("unchecked") //guarded by previous check
                Collection<?> values = ((Constant<? extends Collection<?>>) expr.getArg(constIndex)).getConstant();
                return asDocument(asDBKey(expr, exprIndex), asDocument("$nin", values));
            } else {
                Path<?> path = (Path<?>) expr.getArg(exprIndex);
                Constant<?> constant = (Constant<?>) expr.getArg(constIndex);
                return asDocument(asDBKey(expr, exprIndex), asDocument("$ne", convert(path, constant)));
            }

        } else if (op == Ops.COL_IS_EMPTY) {
            List<Object> list = new ArrayList<Object>(2);
            list.add(asDocument(asDBKey(expr, 0), new ArrayList<Object>()));
            list.add(asDocument(asDBKey(expr, 0), asDocument("$exists", false)));
            return asDocument("$or", list);

        } else if (op == Ops.LT) {
            return asDocument(asDBKey(expr, 0), asDocument("$lt", asDBValue(expr, 1)));

        } else if (op == Ops.GT) {
            return asDocument(asDBKey(expr, 0), asDocument("$gt", asDBValue(expr, 1)));

        } else if (op == Ops.LOE) {
            return asDocument(asDBKey(expr, 0), asDocument("$lte", asDBValue(expr, 1)));

        } else if (op == Ops.GOE) {
            return asDocument(asDBKey(expr, 0), asDocument("$gte", asDBValue(expr, 1)));

        } else if (op == Ops.IS_NULL) {
            return asDocument(asDBKey(expr, 0), asDocument("$exists", false));

        } else if (op == Ops.IS_NOT_NULL) {
            return asDocument(asDBKey(expr, 0), asDocument("$exists", true));

        } else if (op == Ops.CONTAINS_KEY) {
            Path<?> path = (Path<?>) expr.getArg(0);
            Expression<?> key = expr.getArg(1);
            return asDocument(visit(path, context) + "." + key.toString(), asDocument("$exists", true));

        } else if (op == MongodbOps.NEAR) {
            return asDocument(asDBKey(expr, 0), asDocument("$near", asDBValue(expr, 1)));

        } else if (op == MongodbOps.NEAR_SPHERE) {
            return asDocument(asDBKey(expr, 0), asDocument("$nearSphere", asDBValue(expr, 1)));

        } else if (op == MongodbOps.ELEM_MATCH) {
            return asDocument(asDBKey(expr, 0), asDocument("$elemMatch", asDBValue(expr, 1)));
        } else if (op == MongodbOps.NO_MATCH) {
            return new Document("$where", new BsonJavaScript("function() { return false }"));
        }

        throw new UnsupportedOperationException("Illegal operation " + expr);
    }

    private Object negate(Document arg) {
        List<Object> list = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : arg.entrySet()) {
            if (entry.getKey().equals("$or")) {
                list.add(asDocument("$nor", entry.getValue()));

            } else if (entry.getKey().equals("$and")) {
                List<Object> list2 = new ArrayList<Object>();
                for (Object o : ((Collection) entry.getValue())) {
                    list2.add(negate((Document) o));
                }
                list.add(asDocument("$or", list2));

            } else if (entry.getValue() instanceof Pattern || entry.getValue() instanceof BsonRegularExpression) {
                list.add(asDocument(entry.getKey(), asDocument("$not", entry.getValue())));

            } else if (entry.getValue() instanceof Document) {
                list.add(negate(entry.getKey(), (Document) entry.getValue()));

            } else {
                list.add(asDocument(entry.getKey(), asDocument("$ne", entry.getValue())));
            }
        }
        return list.size() == 1 ? list.get(0) : asDocument("$or", list);
    }

    private Object negate(String key, Document value) {
        if (value.size() == 1) {
            return asDocument(key, asDocument("$not", value));

        } else {
            List<Object> list2 = new ArrayList<Object>();
            for (Map.Entry<String, Object> entry2 : value.entrySet()) {
                list2.add(asDocument(key,
                        asDocument("$not", asDocument(entry2.getKey(), entry2.getValue()))));
            }
            return asDocument("$or", list2);
        }
    }

    protected Object convert(Path<?> property, Constant<?> constant) {
        if (isReference(property)) {
            return asReference(constant.getConstant());
        } else if (isId(property)) {
            if (isReference(property.getMetadata().getParent())) {
                return asReferenceKey(property.getMetadata().getParent().getType(), constant.getConstant());
            } else if (constant.getType().equals(String.class) && isImplicitObjectIdConversion()) {
                String id = (String) constant.getConstant();
                return ObjectId.isValid(id) ? new ObjectId(id) : id;
            }
        }
        return visit(constant, null);
    }

    protected boolean isImplicitObjectIdConversion() {
        return true;
    }

    protected DBRef asReferenceKey(Class<?> entity, Object id) {
        // TODO override in subclass
        throw new UnsupportedOperationException();
    }

    protected abstract DBRef asReference(Object constant);

    protected abstract boolean isReference(Path<?> arg);

    protected boolean isId(Path<?> arg) {
        // TODO override in subclass
        return false;
    }

    @Override
    public String visit(Path<?> expr, Void context) {
        PathMetadata metadata = expr.getMetadata();
        if (metadata.getParent() != null) {
            Path<?> parent = metadata.getParent();
            if (parent.getMetadata().getPathType() == PathType.DELEGATE) {
                parent = parent.getMetadata().getParent();
            }
            if (metadata.getPathType() == PathType.COLLECTION_ANY) {
                return visit(parent, context);
            } else if (parent.getMetadata().getPathType() != PathType.VARIABLE) {
                String rv = getKeyForPath(expr, metadata);
                String parentStr = visit(parent, context);
                return rv != null ? parentStr + "." + rv : parentStr;
            }
        }
        return getKeyForPath(expr, metadata);
    }

    protected String getKeyForPath(Path<?> expr, PathMetadata metadata) {
        return metadata.getElement().toString();
    }

    @Override
    public Object visit(SubQueryExpression<?> expr, Void context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(ParamExpression<?> expr, Void context) {
        throw new UnsupportedOperationException();
    }

    private Queue<Map<Object, Object>> collectConnectorArgs(String operator, Operation<?> operation) {

        Queue<Map<Object, Object>> pendingDocuments = new LinkedList<Map<Object, Object>>();
        for (Expression<?> exp : operation.getArgs()) {
            Map<Object, Object> document = (Map<Object, Object>) handle(exp);
            if (document.keySet().size() == 1 && document.containsKey(operator)) {
                pendingDocuments
                        .addAll((Collection<Map<Object, Object>>) document.get(operator));
            } else {
                pendingDocuments.add(document);
            }
        }
        return pendingDocuments;
    }
}
