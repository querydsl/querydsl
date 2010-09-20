/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mysema.query.types.*;

/**
 * Serializes the given QueryDSL query to a DBObject querty format MongoDB
 * understands.
 * 
 * @author laimw
 * 
 */
public class MongodbSerializer implements Visitor<Object, Void> {

    public Object handle(Expression<?> where) {
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
    public Object visit(TemplateExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(FactoryExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        return null;
    }

    private String dboKey(Operation<?> expr, int index) {
        return (String) dboValue(expr, index);
    }

    private Object dboValue(Operation<?> expr, int index) {
        return expr.getArg(index).accept(this, null);
    }

    private String regexValue(Operation<?> expr, int index) {
        return Pattern.quote(expr.getArg(index).accept(this, null).toString());
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
        if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE ) {
            return dbo(dboKey(expr, 0), dboValue(expr, 1));
        }
        if (op == Ops.AND) {
            BasicDBObject left = (BasicDBObject) handle(expr.getArg(0));
            left.putAll((BSONObject) handle(expr.getArg(1)));
            return left;
        }
        if (op == Ops.NOT) {
            //Handle the not's child            
            BasicDBObject arg = (BasicDBObject) handle(expr.getArg(0));
            
            //Only support the first key, let's see if there
            //is cases where this will get broken
            String key = arg.keySet().iterator().next();
            
            Operator<?> subOp = ((Operation<?>) expr.getArg(0)).getOperator();
            if (subOp != Ops.EQ_OBJECT && subOp != Ops.EQ_PRIMITIVE)
                return dbo(key, dbo("$not", arg.get(key)));
            else
                return dbo(key, dbo("$ne", arg.get(key)));
        }
        
//      if (op == Ops.OR)
        if (op == Ops.NE_OBJECT || op == Ops.NE_PRIMITIVE) {
            return dbo(dboKey(expr, 0), dbo("$ne", dboValue(expr, 1)));
        }
        if (op == Ops.STARTS_WITH) {
            return dbo(dboKey(expr, 0), Pattern.compile("^" + regexValue(expr, 1)));
        }
        if (op == Ops.STARTS_WITH_IC) {
            return dbo(dboKey(expr, 0),
                    Pattern.compile("^" + regexValue(expr, 1), Pattern.CASE_INSENSITIVE));
        }
        if (op == Ops.ENDS_WITH) {
            return dbo(dboKey(expr, 0), Pattern.compile(regexValue(expr, 1) + "$"));
        }
        if (op == Ops.ENDS_WITH_IC) {
            return dbo(dboKey(expr, 0),
                    Pattern.compile(regexValue(expr, 1) + "$", Pattern.CASE_INSENSITIVE));
        }
        if (op == Ops.EQ_IGNORE_CASE) {
            return dbo(dboKey(expr, 0),
                    Pattern.compile(regexValue(expr, 1), Pattern.CASE_INSENSITIVE));
        }
        if (op == Ops.STRING_CONTAINS) {
            return dbo(dboKey(expr, 0), Pattern.compile(".*" + regexValue(expr, 1) + ".*"));
        }
        if (op == Ops.STRING_CONTAINS_IC) {
            return dbo(dboKey(expr, 0),
                    Pattern.compile(".*" + regexValue(expr, 1) + ".*", Pattern.CASE_INSENSITIVE));
        }
        if (op == Ops.EQ_IGNORE_CASE) {
            return dbo(dboKey(expr, 0),
                    Pattern.compile(regexValue(expr, 1), Pattern.CASE_INSENSITIVE));
        }
        //if (op == Ops.EXISTS)
//        if (op == Ops.MOD)
         
        if (op == Ops.MATCHES) {
            return dbo(dboKey(expr, 0), Pattern.compile(dboValue(expr, 1).toString()));
        }
        
        if (op == Ops.BETWEEN) {
            BasicDBObject value = new BasicDBObject("$gt", dboValue(expr, 1));
            value.append("$lt", dboValue(expr, 2));
            return dbo(dboKey(expr, 0), value);
        }
        
        if (op == Ops.IN) {
            Collection<?> values = (Collection<?>) ((Constant<?>) expr
                    .getArg(1)).getConstant();
            return dbo(dboKey(expr, 0), dbo("$in", values.toArray()));
        }
        if (op == Ops.LT || op == Ops.BEFORE) {
            return dbo(dboKey(expr, 0), dbo("$lt", dboValue(expr, 1)));
        }
        if (op == Ops.GT || op == Ops.AFTER) {
            return dbo(dboKey(expr, 0), dbo("$gt", dboValue(expr, 1)));
        }
        if (op == Ops.LOE || op == Ops.BOE) {
            return dbo(dboKey(expr, 0), dbo("$lte", dboValue(expr, 1)));
        }
        if (op == Ops.GOE || op == Ops.AOE) {
            return dbo(dboKey(expr, 0), dbo("$gte", dboValue(expr, 1)));
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
    public Object visit(ParamExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        return null;
    }

}