package com.mysema.query.dynamodb.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
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
import com.mysema.query.types.expr.BooleanOperation;

/**
 * JPQLSerializer serializes Querydsl expressions into DynamoDB v2 model.
 *
 * @author velo
 */
public class DynamodbSerializer implements Visitor<Object, DynamoDBScanExpression> {

    public static final DynamodbSerializer DEFAULT = new DynamodbSerializer();

    @Override
    public Object visit(Constant<?> expr, DynamoDBScanExpression scanExpression) {
        Object value = expr.getConstant();
        if (value instanceof String) {
            return new AttributeValue().withS((String) value);
        }
        if (value instanceof Number) {
            return new AttributeValue().withN(String.valueOf(value));
        }
        if (value instanceof String[]) {
            return new AttributeValue().withSS((String[]) value);
        }
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (list.isEmpty()) {
                return new AttributeValue();
            }
            if (list.get(0) instanceof String) {
                @SuppressWarnings("unchecked")
                List<String> values = (List<String>) value;
                return new AttributeValue().withSS(values);
            }
            if (list.get(0) instanceof Number) {
                @SuppressWarnings("unchecked")
                List<Number> values = (List<Number>) value;
                List<String> asString = new ArrayList<String>();
                for (Number number : values) {
                    asString.add(String.valueOf(number));
                }
                return new AttributeValue().withNS(asString);
            }
            throw new UnsupportedOperationException("Unexpected type: List<>:"
                    + list.get(0).getClass());
        }

        throw new UnsupportedOperationException("Unexpected type: " + value.getClass());
    }

    @Override
    public Object visit(FactoryExpression<?> expr, DynamoDBScanExpression scanExpression) {
        // TODO Auto-generated method stub
        throw new RuntimeException();
    }

    @Override
    public Object visit(Operation<?> expr, DynamoDBScanExpression scanExpression) {
        Operator<?> op = expr.getOperator();
        Condition condition = new Condition();
        if (op == Ops.EQ) {
            condition.setComparisonOperator(ComparisonOperator.EQ);
        } else if (op == Ops.STARTS_WITH) {
            condition.setComparisonOperator(ComparisonOperator.BEGINS_WITH);
        } else if (op == Ops.BETWEEN) {
            condition.setComparisonOperator(ComparisonOperator.BETWEEN);
        } else if (op == Ops.STRING_CONTAINS) {
            condition.setComparisonOperator(ComparisonOperator.CONTAINS);
        } else if (op == Ops.GOE) {
            condition.setComparisonOperator(ComparisonOperator.GE);
        } else if (op == Ops.GT) {
            condition.setComparisonOperator(ComparisonOperator.GT);
        } else if (op == Ops.LT) {
            condition.setComparisonOperator(ComparisonOperator.LT);
        } else if (op == Ops.LOE) {
            condition.setComparisonOperator(ComparisonOperator.LE);
        } else if (op == Ops.IN) {
            condition.setComparisonOperator(ComparisonOperator.IN);
        } else if (op == Ops.NE) {
            condition.setComparisonOperator(ComparisonOperator.NE);
        } else if (op == Ops.IS_NULL) {
            condition.setComparisonOperator(ComparisonOperator.NULL);
        } else if (op == Ops.IS_NOT_NULL) {
            condition.setComparisonOperator(ComparisonOperator.NOT_NULL);
        } else if (op == Ops.NOT) {
            Expression<?> arg = expr.getArg(0);
            if (arg instanceof BooleanOperation) {
                Operator<? super Boolean> subOp = ((BooleanOperation) arg).getOperator();
                if (subOp == Ops.STRING_CONTAINS) {
                    condition.setComparisonOperator(ComparisonOperator.NOT_CONTAINS);
                    expr = (Operation<?>) arg;
                } else {
                    throw new UnsupportedOperationException(String.valueOf(op));
                }
            } else {
                throw new UnsupportedOperationException(String.valueOf(op));
            }
        } else if (op == Ops.AND) {
            List<Expression<?>> args = expr.getArgs();
            for (Expression<?> expression : args) {
                expression.accept(this, scanExpression);
            }
            return null;
        } else {
            throw new UnsupportedOperationException(String.valueOf(op));
        }

        populateScanExpression(expr.getArgs(), condition, scanExpression);
        return condition;

    }

    private void populateScanExpression(List<Expression<?>> expressions, Condition condition,
            DynamoDBScanExpression scanExpression) {

        List<AttributeValue> attributeValueList = new ArrayList<AttributeValue>();

        String attributeName = null;
        for (Expression<?> expression : expressions) {
            Object result = expression.accept(this, scanExpression);
            if (result instanceof String) {
                if (attributeName != null) {
                    throw new RuntimeException("Already in use");
                }
                attributeName = (String) result;
            } else if (result instanceof AttributeValue) {
                attributeValueList.add((AttributeValue) result);
            } else {
                throw new UnsupportedOperationException("Invalid result: " + result);
            }
        }

        condition.setAttributeValueList(attributeValueList);
        scanExpression.addFilterCondition(attributeName, condition);
    }

    @Override
    public Object visit(ParamExpression<?> expr, DynamoDBScanExpression scanExpression) {
        // TODO Auto-generated method stub
        throw new RuntimeException();
    }

    @Override
    public Object visit(Path<?> expr, DynamoDBScanExpression scanExpression) {
        AnnotatedElement element = expr.getAnnotatedElement();
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof DynamoDBRangeKey) {
                return expr.getMetadata().getElement();
            }
        }

        return expr.getMetadata().getElement();
    }

    @Override
    public Object visit(SubQueryExpression<?> expr, DynamoDBScanExpression scanExpression) {
        // TODO Auto-generated method stub
        throw new RuntimeException();
    }

    @Override
    public Object visit(TemplateExpression<?> expr, DynamoDBScanExpression scanExpression) {
        // TODO Auto-generated method stub
        throw new RuntimeException();
    }

    public DynamoDBScanExpression handle(Predicate predicate) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        predicate.accept(this, scanExpression);
        return scanExpression;
    }

}
