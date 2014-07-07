package com.mysema.query.hazelcast.impl;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.hazelcast.query.Predicates;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.Visitor;

/**
 * JPQLSerializer serializes Querydsl expressions into Hazelcast query model.
 * 
 * @author velo
 */
public class HazelcastSerializer implements Visitor<Object, Void> {

    public static final HazelcastSerializer DEFAULT = new HazelcastSerializer();

    @Override
    public Object visit(Constant<?> expr, Void context) {
        return expr.getConstant();
    }

    @Override
    public Object visit(FactoryExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        throw new RuntimeException();
    }

    @Override
    public com.hazelcast.query.Predicate visit(Operation<?> expr, final Void context) {
        Operator<?> op = expr.getOperator();
        List<Expression<?>> args = expr.getArgs();

        if (op == Ops.AND) {
            com.hazelcast.query.Predicate leftCondition = (com.hazelcast.query.Predicate) args.get(
                    0).accept(this, context);
            com.hazelcast.query.Predicate rightCondition = (com.hazelcast.query.Predicate) args
                    .get(1).accept(this, context);
            return Predicates.and(leftCondition, rightCondition);
        } else if (op == Ops.OR) {
            com.hazelcast.query.Predicate leftCondition = (com.hazelcast.query.Predicate) args.get(
                    0).accept(this, context);
            com.hazelcast.query.Predicate rightCondition = (com.hazelcast.query.Predicate) args
                    .get(1).accept(this, context);
            return Predicates.or(leftCondition, rightCondition);
        } else if (op == Ops.NOT) {
            com.hazelcast.query.Predicate predicate = (com.hazelcast.query.Predicate) args.get(0)
                    .accept(this, context);
            return Predicates.not(predicate);
        } else {
            String attr = (String) args.get(0).accept(this, context);
            if (op == Ops.IN) {
                List<Comparable> values = (List<Comparable>) args.get(1).accept(this, context);
                return Predicates.in(attr, values.toArray(new Comparable[0]));
            } else if (op == Ops.NOT_IN) {
                List<Comparable> values = (List<Comparable>) args.get(1).accept(this, context);
                return Predicates.not(Predicates.in(attr, values.toArray(new Comparable[0])));
            } else if (args.size() == 2) {
                Comparable value = (Comparable) args.get(1).accept(this, context);
                if (op == Ops.EQ) {
                    return Predicates.equal(attr, value);
                } else if (op == Ops.GOE) {
                    return Predicates.greaterEqual(attr, value);
                } else if (op == Ops.GT) {
                    return Predicates.greaterThan(attr, value);
                } else if (op == Ops.LT) {
                    return Predicates.lessThan(attr, value);
                } else if (op == Ops.LOE) {
                    return Predicates.lessEqual(attr, value);
                } else if (op == Ops.NE) {
                    return Predicates.notEqual(attr, value);
                } else if (op == Ops.LIKE) {
                    return Predicates.like(attr, (String) value);
                } else {
                    throw new UnsupportedOperationException(op.getId());
                }
            } else if (op == Ops.BETWEEN) {
                List<Comparable> values = Lists.transform(args,
                        new Function<Expression<?>, Comparable>() {
                            @Override
                            public Comparable apply(Expression<?> input) {
                                return (Comparable) input.accept(HazelcastSerializer.this, context);
                            }
                        });
                values = Lists.newArrayList(values);
                values.remove(0);
                return Predicates.between(attr, values.get(0), values.get(1));
            } else if (op == Ops.IS_NULL) {
                return Predicates.equal(attr, null);
            } else if (op == Ops.IS_NOT_NULL) {
                return Predicates.notEqual(attr, null);
            } else {
                throw new UnsupportedOperationException(String.valueOf(op));
            }
        }
    }

    @Override
    public Object visit(ParamExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        throw new RuntimeException();
    }

    @Override
    public Object visit(Path<?> expr, Void context) {
        String path = expr.toString();
        return path.substring(path.indexOf('.') + 1);
    }

    @Override
    public Object visit(SubQueryExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(TemplateExpression<?> expr, Void context) {
        // TODO Auto-generated method stub
        throw new RuntimeException();
    }

    public com.hazelcast.query.Predicate handle(Predicate predicate) {
        return (com.hazelcast.query.Predicate) predicate.accept(this, null);
    }

}
