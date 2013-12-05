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
package com.mysema.query.mongodb;

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
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathType;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.Visitor;

/**
 * Serializes the given Querydsl query to a DBObject query for MongoDB
 *
 * @author laimw
 *
 */
public abstract class MongodbSerializer<Context> implements Visitor<Object, Context> {

    public Object handle(Expression<?> expression, Context context) {
        return expression.accept(this, context);
    }

    public DBObject toSort(List<OrderSpecifier<?>> orderBys, Context context) {
        BasicDBObject sort = new BasicDBObject();
        for (OrderSpecifier<?> orderBy : orderBys) {
            Object key = orderBy.getTarget().accept(this, context);
            sort.append(key.toString(), orderBy.getOrder() == Order.ASC ? 1 : -1);
        }
        return sort;
    }

    @Override
    public Object visit(Constant<?> expr, Context context) {
        if (Enum.class.isAssignableFrom(expr.getType())) {
            return ((Enum<?>)expr.getConstant()).name();
        } else {
            return expr.getConstant();
        }
    }

    @Override
    public Object visit(TemplateExpression<?> expr, Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(FactoryExpression<?> expr, Context context) {
        throw new UnsupportedOperationException();
    }

    private String asDBKey(Operation<?> expr, int index, Context context) {
        return (String) asDBValue(expr, index, context);
    }

    private Object asDBValue(Operation<?> expr, int index, Context context) {
        return expr.getArg(index).accept(this, context);
    }

    private String regexValue(Operation<?> expr, int index, Context context) {
        return Pattern.quote(expr.getArg(index).accept(this, context).toString());
    }

    protected DBObject asDBObject(String key, Object value) {
        return new BasicDBObject(key, value);
    }

    @Override
    public Object visit(Operation<?> expr, Context context) {
        Operator<?> op = expr.getOperator();
        if (op == Ops.EQ) {
            if (expr.getArg(0) instanceof Operation) {
                Operation<?> lhs = (Operation<?>)expr.getArg(0);
                if (lhs.getOperator() == Ops.COL_SIZE || lhs.getOperator() == Ops.ARRAY_SIZE) {
                    return asDBObject(asDBKey(lhs, 0, context), asDBObject("$size", asDBValue(expr, 1, context)));
                } else {
                    throw new UnsupportedOperationException("Illegal operation " + expr);
                }
            } else {
                return asDBObject(asDBKey(expr, 0, context), asDBValue(expr, 1, context));
            }

        } else if (op == Ops.STRING_IS_EMPTY) {
            return asDBObject(asDBKey(expr, 0, context), "");

        } else if (op == Ops.AND) {
            BSONObject lhs = (BSONObject) handle(expr.getArg(0), context);
            BSONObject rhs = (BSONObject) handle(expr.getArg(1), context);
            if (Sets.intersection(lhs.keySet(), rhs.keySet()).isEmpty()) {
                lhs.putAll(rhs);
                return lhs;
            } else {
                BasicDBList list = new BasicDBList();
                list.add(handle(expr.getArg(0), context));
                list.add(handle(expr.getArg(1), context));
                return asDBObject("$and", list);
            }

        } else if (op == Ops.NOT) {
            //Handle the not's child
            BasicDBObject arg = (BasicDBObject) handle(expr.getArg(0), context);

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
            list.add(handle(expr.getArg(0), context));
            list.add(handle(expr.getArg(1), context));
            return asDBObject("$or", list);

        } else if (op == Ops.NE) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$ne", asDBValue(expr, 1, context)));

        } else if (op == Ops.STARTS_WITH) {
            return asDBObject(asDBKey(expr, 0, context),
                    Pattern.compile("^" + regexValue(expr, 1, context)));

        } else if (op == Ops.STARTS_WITH_IC) {
            return asDBObject(asDBKey(expr, 0, context),
                    Pattern.compile("^" + regexValue(expr, 1, context), Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.ENDS_WITH) {
            return asDBObject(asDBKey(expr, 0, context), Pattern.compile(regexValue(expr, 1, context) + "$"));

        } else if (op == Ops.ENDS_WITH_IC) {
            return asDBObject(asDBKey(expr, 0, context),
                    Pattern.compile(regexValue(expr, 1, context) + "$", Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.EQ_IGNORE_CASE) {
            return asDBObject(asDBKey(expr, 0, context),
                    Pattern.compile("^" + regexValue(expr, 1, context) + "$", Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.STRING_CONTAINS) {
            return asDBObject(asDBKey(expr, 0, context), Pattern.compile(".*" + regexValue(expr, 1, context) + ".*"));

        } else if (op == Ops.STRING_CONTAINS_IC) {
            return asDBObject(asDBKey(expr, 0, context),
                    Pattern.compile(".*" + regexValue(expr, 1, context) + ".*", Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.MATCHES) {
            return asDBObject(asDBKey(expr, 0, context), Pattern.compile(asDBValue(expr, 1, context).toString()));

        } else if (op == Ops.MATCHES_IC) {
            return asDBObject(asDBKey(expr, 0, context), Pattern.compile(asDBValue(expr, 1, context).toString(), Pattern.CASE_INSENSITIVE));

        } else if (op == Ops.LIKE) {
            String regex = ExpressionUtils.likeToRegex((Expression)expr.getArg(1)).toString();
            return asDBObject(asDBKey(expr, 0, context), Pattern.compile(regex));

        } else if (op == Ops.BETWEEN) {
            BasicDBObject value = new BasicDBObject("$gte", asDBValue(expr, 1, context));
            value.append("$lte", asDBValue(expr, 2, context));
            return asDBObject(asDBKey(expr, 0, context), value);

        } else if (op == Ops.IN) {
            int constIndex = 0;
            int exprIndex = 1;
            if (expr.getArg(1) instanceof Constant<?>) {
                constIndex = 1;
                exprIndex = 0;
            }
            if (Collection.class.isAssignableFrom(expr.getArg(constIndex).getType())) {
                Collection<?> values = (Collection<?>) ((Constant<?>) expr.getArg(constIndex)).getConstant();
                return asDBObject(asDBKey(expr, exprIndex, context), asDBObject("$in", values.toArray()));
            } else {
                if (isReference(expr, exprIndex, context)) {
                    return asDBObject(asDBKey(expr, exprIndex, context), asReference(expr, constIndex, context));
                } else {
                    return asDBObject(asDBKey(expr, exprIndex, context), asDBValue(expr, constIndex, context));
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
                return asDBObject(asDBKey(expr, exprIndex, context), asDBObject("$nin", values.toArray()));
            } else {
                if (isReference(expr, exprIndex, context)) {
                    return asDBObject(asDBKey(expr, exprIndex, context),
                            asDBObject("$ne", asReference(expr, constIndex, context)));
                } else {
                    return asDBObject(asDBKey(expr, exprIndex, context),
                            asDBObject("$ne", asDBValue(expr, constIndex, context)));
                }
            }

        } else if (op == Ops.COL_IS_EMPTY) {
            BasicDBList list = new BasicDBList();
            list.add(asDBObject(asDBKey(expr, 0, context), new BasicDBList()));
            list.add(asDBObject(asDBKey(expr, 0, context), asDBObject("$exists", false)));
            return asDBObject("$or", list);

        } else if (op == Ops.LT) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$lt", asDBValue(expr, 1, context)));

        } else if (op == Ops.GT) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$gt", asDBValue(expr, 1, context)));

        } else if (op == Ops.LOE) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$lte", asDBValue(expr, 1, context)));

        } else if (op == Ops.GOE) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$gte", asDBValue(expr, 1, context)));

        } else if (op == Ops.IS_NULL) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$exists", false));

        } else if (op == Ops.IS_NOT_NULL) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$exists", true));

        } else if (op == Ops.CONTAINS_KEY) {
            Path<?> path = (Path<?>) expr.getArg(0);
            Expression<?> key = expr.getArg(1);
            return asDBObject(visit(path, context) + "." + key.toString(), asDBObject("$exists", true));

        } else if (op == MongodbOps.NEAR) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$near", asDBValue(expr, 1, context)));

        } else if (op == MongodbOps.ELEM_MATCH) {
            return asDBObject(asDBKey(expr, 0, context), asDBObject("$elemMatch", asDBValue(expr, 1, context)));
        }

        throw new UnsupportedOperationException("Illegal operation " + expr);
    }

    protected DBRef asReference(Operation<?> expr, int constIndex, Context context) {
        return asReference(((Constant<?>)expr.getArg(constIndex)).getConstant(), context);
    }

    protected abstract DBRef asReference(Object constant, Context context);

    protected boolean isReference(Operation<?> expr, int exprIndex, Context context) {
        Expression<?> arg = expr.getArg(exprIndex);
        if (arg instanceof Path) {
            return isReference((Path<?>) arg, context);
        } else {
            return false;
        }
    }

    protected abstract boolean isReference(Path<?> arg, Context context);

    @Override
    public String visit(Path<?> expr, Context context) {
        PathMetadata<?> metadata = expr.getMetadata();
        if (metadata.getParent() != null) {
            if (metadata.getPathType() == PathType.COLLECTION_ANY) {
                return visit(metadata.getParent(), context);
            } else if (metadata.getParent().getMetadata().getPathType() != PathType.VARIABLE) {
                String rv = getKeyForPath(expr, metadata, context);
                return visit(metadata.getParent(), context) + "." + rv;
            }
        }
        return getKeyForPath(expr, metadata, context);
    }

    protected String getKeyForPath(Path<?> expr, PathMetadata<?> metadata, Context context) {
        if (expr.getType().equals(ObjectId.class)) {
            return "_id";
        } else {
            return metadata.getElement().toString();
        }
    }

    @Override
    public Object visit(SubQueryExpression<?> expr, Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(ParamExpression<?> expr, Context context) {
        throw new UnsupportedOperationException();
    }

}