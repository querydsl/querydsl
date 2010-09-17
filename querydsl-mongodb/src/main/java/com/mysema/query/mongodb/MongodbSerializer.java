package com.mysema.query.mongodb;

import java.util.Collection;
import java.util.List;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Custom;
import com.mysema.query.types.Expr;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Visitor;

/**
 * Serializes the given QueryDSL query to a DBObject querty format MongoDB
 * understands.
 * 
 * @author laimw
 * 
 */
public class MongodbSerializer implements Visitor<Object, Void> {

    public Object handle(Expr<?> where) {
        return where.accept(this, null);
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
        return expr.getConstant();
    }

    @Override
    public Object visit(Custom<?> expr, Void context) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(FactoryExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        return null;
    }

    private String asString(Operation<?> expr, int index) {
        return (String) expr.getArg(index).accept(this, null);
    }

    private Object asObj(Operation<?> expr, int index) {
        return expr.getArg(index).accept(this, null);
    }

    private BasicDBObject dbo(String key, Object value) {
        return new BasicDBObject(key, value);
    }

    @Override
    public Object visit(Operation<?> expr, Void context) {
        Operator<?> op = expr.getOperator();
        // if (op == Ops.OR) {
        // return toTwoHandSidedQuery(operation, Occur.SHOULD, metadata);
        // } else if (op == Ops.NOT) {
        // BooleanQuery bq = new BooleanQuery();
        // bq.add(new BooleanClause(toQuery(operation.getArg(0), metadata),
        // Occur.MUST_NOT));
        // return bq;
        // } else if (op == Ops.LIKE) {
        // return like(operation, metadata);
        if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE /*
                                                           * || op ==
                                                           * Ops.EQ_IGNORE_CASE
                                                           */) {
            return dbo(asString(expr, 0), asObj(expr, 1));
        }
        if (op == Ops.AND) {
            BasicDBObject left = (BasicDBObject) handle(expr.getArg(0));
            left.putAll((BSONObject) handle(expr.getArg(1)));
            return left;
        }
        if (op == Ops.NE_OBJECT || op == Ops.NE_PRIMITIVE) {
            return dbo(asString(expr, 0), dbo("$ne", asObj(expr, 1)));
        }
        // } else if (op == Ops.STARTS_WITH || op == Ops.STARTS_WITH_IC) {
        // return startsWith(metadata, operation);
        // } else if (op == Ops.ENDS_WITH || op == Ops.ENDS_WITH_IC) {
        // return endsWith(operation, metadata);
        // } else if (op == Ops.STRING_CONTAINS || op == Ops.STRING_CONTAINS_IC)
        // {
        // return stringContains(operation, metadata);
        if (op == Ops.BETWEEN) {
            BasicDBObject value = new BasicDBObject("$gt", asObj(expr, 1));
            value.append("$lt", asObj(expr, 2));
            return dbo(asString(expr, 0), value);
        }
        if (op == Ops.IN) {
            Collection<?> values = (Collection<?>) ((Constant<?>) expr
                    .getArg(1)).getConstant();
            return dbo(asString(expr, 0), dbo("$in", values.toArray()));
        }
        if (op == Ops.LT || op == Ops.BEFORE) {
            return dbo(asString(expr, 0), dbo("$lt", asObj(expr, 1)));
        }
        if (op == Ops.GT || op == Ops.AFTER) {
            return dbo(asString(expr, 0), dbo("$gt", asObj(expr, 1)));
        }
        if (op == Ops.LOE || op == Ops.BOE) {
            return dbo(asString(expr, 0), dbo("$lte", asObj(expr, 1)));
        }
        if (op == Ops.GOE || op == Ops.AOE) {
            return dbo(asString(expr, 0), dbo("$gte", asObj(expr, 1)));
        }
        // } else if (op == PathType.DELEGATE) {
        // return toQuery(operation.getArg(0), metadata);
        // }
        throw new UnsupportedOperationException("Illegal operation " + expr);
    }

    @Override
    public Object visit(Path<?> expr, Void context) {
        return expr.getMetadata().getExpression().toString();
    }

    @Override
    public Object visit(SubQueryExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(Param<?> expr, Void context) {
        // TODO Auto-generated method stub
        return null;
    }

}