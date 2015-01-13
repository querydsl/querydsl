/*
 * Copyright 2011, Mysema Ltd
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
import java.util.regex.Pattern;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.TemplateExpression;
import com.querydsl.core.types.Visitor;

/**
 * Serializes the given Querydsl querydsl to a DBObject querydsl for MongoDB
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
            return ((Enum<?>)expr.getConstant()).name();
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

    @Override
    public Object visit(Operation<?> expr, Void context) {
        Operator<?> op = expr.getOperator();
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
            BasicDBObject arg = (BasicDBObject) handle(expr.getArg(0));

            //Only support the first key, let's see if there
            //is cases where this will get broken
            String key = arg.keySet().iterator().next();
            Operation<?> subOperation = (Operation<?>) expr.getArg(0);
            Operator<?> subOp = subOperation.getOperator();
            if (subOp == Ops.IN) {
                return visit(OperationImpl.create(Boolean.class, Ops.NOT_IN, subOperation.getArg(0),
                        subOperation.getArg(1)), context);
            } else if (subOp != Ops.EQ && subOp != Ops.STRING_IS_EMPTY) {
                return asDBObject(key, asDBObject("$not", arg.get(key)));
            } else {
                return asDBObject(key, asDBObject("$ne", arg.get(key)));
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
            String regex = ExpressionUtils.likeToRegex((Expression)expr.getArg(1)).toString();
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
                Collection<?> values = (Collection<?>) ((Constant<?>) expr.getArg(constIndex)).getConstant();
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
                Collection<?> values = (Collection<?>) ((Constant<?>) expr.getArg(constIndex)).getConstant();
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

        } else if (op == MongodbOps.ELEM_MATCH) {
            return asDBObject(asDBKey(expr, 0), asDBObject("$elemMatch", asDBValue(expr, 1)));
        }

        throw new UnsupportedOperationException("Illegal operation " + expr);
    }

    protected DBRef asReference(Operation<?> expr, int constIndex) {
        return asReference(((Constant<?>)expr.getArg(constIndex)).getConstant());
    }

    protected DBRef asReference(Object constant) {
        // override in subclass
        throw new UnsupportedOperationException();
    }

    protected boolean isReference(Operation<?> expr, int exprIndex) {
        Expression<?> arg = expr.getArg(exprIndex);
        if (arg instanceof Path) {
            return isReference((Path<?>) arg);
        } else {
            return false;
        }
    }

    protected boolean isReference(Path<?> arg) {
        // override in subclass
        return false;
    }


    @Override
    public String visit(Path<?> expr, Void context) {
        PathMetadata<?> metadata = expr.getMetadata();
        if (metadata.getParent() != null) {
            if (metadata.getPathType() == PathType.COLLECTION_ANY) {
                return visit(metadata.getParent(), context);
            } else if (metadata.getParent().getMetadata().getPathType() != PathType.VARIABLE) {
                String rv = getKeyForPath(expr, metadata);
                return visit(metadata.getParent(), context) + "." + rv;
            }
        }
        return getKeyForPath(expr, metadata);
    }

    protected String getKeyForPath(Path<?> expr, PathMetadata<?> metadata) {
        if (expr.getType().equals(ObjectId.class)) {
            return "_id";
        } else {
            return metadata.getElement().toString();
        }
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