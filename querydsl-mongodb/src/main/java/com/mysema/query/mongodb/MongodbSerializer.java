package com.mysema.query.mongodb;

import com.mongodb.BasicDBObject;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Custom;
import com.mysema.query.types.Expr;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Visitor;

/**
 * Serializes the given QueryDSL query to a DBObject querty format MongoDB understands.
 * @author laimw
 *
 */
public class MongodbSerializer implements Visitor<Object, Void> {

        public MongodbSerializer() {
            //BasicDBObject o = new BasicDBObject();
            //o.append("firstName", "Juuso");
            
            //o.append("firstName", new BasicDBObject("$ne", "Juuso"));
            //o.append("age", 3);
        }

        
        
        public Object handle(Expr<?> where) {
            return where.accept(this, null);
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

        @Override
        public Object visit(Operation<?> expr, Void context) {
            Operator<?> op = expr.getOperator();
//            if (op == Ops.OR) {
//                return toTwoHandSidedQuery(operation, Occur.SHOULD, metadata);
//            } else if (op == Ops.AND) {
//                return toTwoHandSidedQuery(operation, Occur.MUST, metadata);
//            } else if (op == Ops.NOT) {
//                BooleanQuery bq = new BooleanQuery();
//                bq.add(new BooleanClause(toQuery(operation.getArg(0), metadata), Occur.MUST_NOT));
//                return bq;
//            } else if (op == Ops.LIKE) {
//                return like(operation, metadata);
            if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE || op == Ops.EQ_IGNORE_CASE) {
                return new BasicDBObject((String)expr.getArg(0).accept(this, null), expr.getArg(1).accept(this, null));
            } 
//            else if (op == Ops.NE_OBJECT || op == Ops.NE_PRIMITIVE) {
//                return ne(operation, metadata);
//            } else if (op == Ops.STARTS_WITH || op == Ops.STARTS_WITH_IC) {
//                return startsWith(metadata, operation);
//            } else if (op == Ops.ENDS_WITH || op == Ops.ENDS_WITH_IC) {
//                return endsWith(operation, metadata);
//            } else if (op == Ops.STRING_CONTAINS || op == Ops.STRING_CONTAINS_IC) {
//                return stringContains(operation, metadata);
//            } else if (op == Ops.BETWEEN) {
//                return between(operation, metadata);
//            } else if (op == Ops.IN) {
//                return in(operation, metadata);
//            } else if (op == Ops.LT || op == Ops.BEFORE) {
//                return lt(operation, metadata);
//            } else if (op == Ops.GT || op == Ops.AFTER) {
//                return gt(operation, metadata);
//            } else if (op == Ops.LOE || op == Ops.BOE) {
//                return le(operation, metadata);
//            } else if (op == Ops.GOE || op == Ops.AOE) {
//                return ge(operation, metadata);
//            } else if (op == PathType.DELEGATE) {
//                return toQuery(operation.getArg(0), metadata);
//            }
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