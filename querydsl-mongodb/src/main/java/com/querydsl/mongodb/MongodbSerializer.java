/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.mongodb;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.BSONObject;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.querydsl.core.types.*;

/**
 * Serializes the given Querydsl query to a DBObject query for MongoDB
 *
 * @author laimw
 *
 */
public abstract class MongodbSerializer implements Visitor<Object, Void> {

    public Object handle(Expression<?> expression) {
        return expression.accept(this, null);
    }

    public DBObject toSort(List<OrderSpecifier<?>> orderBys) {
        BasicDBObject sort = new BasicDBObject();
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

    private String asDBKey(Operation<?> expr, int index) {
        return (String) asDBValue(expr, index);
    }

    private Object asDBValue(Operation<?> expr, int index) {
        return expr.getArg(index).accept(this, null);
    }

    private String regexValue(Operation<?> expr, int index) {
        return Pattern.quote(expr.getArg(index).accept(this, null).toString());
    }

    protected DBObject asDBObject(String key, Object value) {
        return new BasicDBObject(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visit(Operation<?> expr, Void context) {
        Operator op = expr.getOperator();
        if (op == Ops.EQ) {
            if (expr.getArg(0) instanceof Operation) {
                Operation<?> lhs = (Operation<?>) expr.getArg(0);
                if (lhs.getOperator() == Ops.COL_SIZE || lhs.getOperator() == Ops.ARRAY_SIZE) {
                    return asDBObject(asDBKey(lhs, 0), asDBObject("$size", asDBValue(expr, 1)));
                } else {
                    throw new UnsupportedOperationException("Illegal operation " + expr);
                }
            } else if (isReference(expr, 0)) {
                return asDBObject(asDBKey(expr, 0), asReference(expr, 1));
            } else {
                return asDBObject(asDBKey(expr, 0), asDBValue(expr, 1));
            }

        } else if (op == Ops.STRING_IS_EMPTY) {
            return asDBObject(asDBKey(expr, 0), "");

        } else if (op == Ops.AND) {
            BSONObject lhs = (BSONObject) handle(expr.getArg(0));
            BSONObject rhs = (BSONObject) handle(expr.getArg(1));
            if (Sets.intersection(lhs.keySet(), rhs.keySet()).isEmpty()) {
                lhs.putAll(rhs);
                return lhs;
            } else {
                BasicDBList list = new BasicDBList();
                list.add(handle(expr.getArg(0)));
                list.add(handle(expr.getArg(1)));
                return asDBObject("$and", list);
            }

        } else if (op == Ops.NOT) {
            //Handle the not's child
            Operation<?> subOperation = (Operation<?>) expr.getArg(0);
            Operator subOp = subOperation.getOperator();
            if (subOp == Ops.IN) {
                return visit(ExpressionUtils.operation(Boolean.class, Ops.NOT_IN, subOperation.getArg(0),
                        subOperation.getArg(1)), context);
            } else {
                BasicDBObject arg = (BasicDBObject) handle(expr.getArg(0));
                return negate(arg);
            }

        } else if (op == Ops.OR) {
            BasicDBList list = new BasicDBList();
            list.add(handle(expr.getArg(0)));
            list.add(handle(expr.getArg(1)));
            return asDBObject("$or", list);

        } else if (op == Ops.NE) {
            if (isReference(expr, 0)) {
                return asDBObject(asDBKey(expr, 0), asDBObject("$ne", asReference(expr, 1)));
            } else {
                return asDBObject(asDBKey(expr, 0), asDBObject("$ne", asDBValue(expr, 1)));
            }

        } else if (op == Ops.STARTS_WITH) {
            return asDBObject(asDBKey(expr, 0),
                    Pattern.compile("^" + regexValue(expr, 1)));

        } else if (op == Ops.STARTS_WITH_IC) {
            return asDBObject(asDBKey(expr, 0),
                    Pattern.compile("^" + regexValue(expr, 1), Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.ENDS_WITH) {
            return asDBObject(asDBKey(expr, 0), Pattern.compile(regexValue(expr, 1) + "$"));

        } else if (op == Ops.ENDS_WITH_IC) {
            return asDBObject(asDBKey(expr, 0),
                    Pattern.compile(regexValue(expr, 1) + "$", Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.EQ_IGNORE_CASE) {
            return asDBObject(asDBKey(expr, 0),
                    Pattern.compile("^" + regexValue(expr, 1) + "$", Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.STRING_CONTAINS) {
            return asDBObject(asDBKey(expr, 0), Pattern.compile(".*" + regexValue(expr, 1) + ".*"));

        } else if (op == Ops.STRING_CONTAINS_IC) {
            return asDBObject(asDBKey(expr, 0),
                    Pattern.compile(".*" + regexValue(expr, 1) + ".*", Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.MATCHES) {
            return asDBObject(asDBKey(expr, 0), Pattern.compile(asDBValue(expr, 1).toString()));

        } else if (op == Ops.MATCHES_IC) {
            return asDBObject(asDBKey(expr, 0), Pattern.compile(asDBValue(expr, 1).toString(), Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.LIKE) {
            String regex = ExpressionUtils.likeToRegex((Expression) expr.getArg(1)).toString();
            return asDBObject(asDBKey(expr, 0), Pattern.compile(regex));

        } else if (op == Ops.BETWEEN) {
            BasicDBObject value = new BasicDBObject("$gte", asDBValue(expr, 1));
            value.append("$lte", asDBValue(expr, 2));
            return asDBObject(asDBKey(expr, 0), value);

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
                return asDBObject(asDBKey(expr, exprIndex), asDBObject("$in", values.toArray()));
            } else {
                if (isReference(expr, exprIndex)) {
                    return asDBObject(asDBKey(expr, exprIndex), asReference(expr, constIndex));
                } else {
                    return asDBObject(asDBKey(expr, exprIndex), asDBValue(expr, constIndex));
                }
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
                return asDBObject(asDBKey(expr, exprIndex), asDBObject("$nin", values.toArray()));
            } else {
                if (isReference(expr, exprIndex)) {
                    return asDBObject(asDBKey(expr, exprIndex),
                            asDBObject("$ne", asReference(expr, constIndex)));
                } else {
                    return asDBObject(asDBKey(expr, exprIndex),
                            asDBObject("$ne", asDBValue(expr, constIndex)));
                }
            }

        } else if (op == Ops.COL_IS_EMPTY) {
            BasicDBList list = new BasicDBList();
            list.add(asDBObject(asDBKey(expr, 0), new BasicDBList()));
            list.add(asDBObject(asDBKey(expr, 0), asDBObject("$exists", false)));
            return asDBObject("$or", list);

        } else if (op == Ops.LT) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$lt", asDBValue(expr, 1)));

        } else if (op == Ops.GT) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$gt", asDBValue(expr, 1)));

        } else if (op == Ops.LOE) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$lte", asDBValue(expr, 1)));

        } else if (op == Ops.GOE) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$gte", asDBValue(expr, 1)));

        } else if (op == Ops.IS_NULL) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$exists", false));

        } else if (op == Ops.IS_NOT_NULL) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$exists", true));

        } else if (op == Ops.CONTAINS_KEY) {
            Path<?> path = (Path<?>) expr.getArg(0);
            Expression<?> key = expr.getArg(1);
            return asDBObject(visit(path, context) + "." + key.toString(), asDBObject("$exists", true));

        } else if (op == MongodbOps.NEAR) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$near", asDBValue(expr, 1)));

        } else if (op == MongodbOps.NEAR_SPHERE) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$nearSphere", asDBValue(expr, 1)));

        } else if (op == MongodbOps.ELEM_MATCH) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$elemMatch", asDBValue(expr, 1)));
        }

        throw new UnsupportedOperationException("Illegal operation " + expr);
    }

    private Object negate(BasicDBObject arg) {
        BasicDBList list = new BasicDBList();
        for (Map.Entry<String, Object> entry : arg.entrySet()) {
            if (entry.getKey().equals("$or")) {
                list.add(asDBObject("$nor", entry.getValue()));

            } else if (entry.getKey().equals("$and")) {
                BasicDBList list2 = new BasicDBList();
                for (Object o : ((BasicDBList) entry.getValue())) {
                    list2.add(negate((BasicDBObject) o));
                }
                list.add(asDBObject("$or", list2));

            } else if (entry.getValue() instanceof Pattern) {
                list.add(asDBObject(entry.getKey(), asDBObject("$not", entry.getValue())));

            } else if (entry.getValue() instanceof BasicDBObject) {
                list.add(negate(entry.getKey(), (BasicDBObject) entry.getValue()));

            } else {
                list.add(asDBObject(entry.getKey(), asDBObject("$ne", entry.getValue())));
            }
        }
        return list.size() == 1 ? list.get(0) : asDBObject("$or", list);
    }

    private Object negate(String key, BasicDBObject value) {
        if (value.size() == 1) {
            return asDBObject(key, asDBObject("$not", value));

        } else {
            BasicDBList list2 = new BasicDBList();
            for (Map.Entry<String, Object> entry2 : value.entrySet()) {
                list2.add(asDBObject(key,
                        asDBObject("$not", asDBObject(entry2.getKey(), entry2.getValue()))));
            }
            return asDBObject("$or", list2);
        }
    }

    protected DBRef asReference(Operation<?> expr, int constIndex) {
        return asReference(((Constant<?>) expr.getArg(constIndex)).getConstant());
    }

    protected abstract DBRef asReference(Object constant);

    protected boolean isReference(Operation<?> expr, int exprIndex) {
        Expression<?> arg = expr.getArg(exprIndex);
        if (arg instanceof Path) {
            return isReference((Path<?>) arg);
        } else {
            return false;
        }
    }

    protected abstract boolean isReference(Path<?> arg);

    @Override
    public String visit(Path<?> expr, Void context) {
        PathMetadata metadata = expr.getMetadata();
        if (metadata.getParent() != null) {
            if (metadata.getPathType() == PathType.COLLECTION_ANY) {
                return visit(metadata.getParent(), context);
            } else if (metadata.getParent().getMetadata().getPathType() != PathType.VARIABLE
                    && metadata.getParent().getMetadata().getPathType() != PathType.DELEGATE) {
                String rv = getKeyForPath(expr, metadata);
                return visit(metadata.getParent(), context) + "." + rv;
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

}
