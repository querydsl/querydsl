package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.MathExpressions;
import com.querydsl.core.types.path.NumberPath;

public class MathTest {
    
    private NumberPath<Double> num = new NumberPath<Double>(Double.class, "num");
    
    @Test
    public void Math() {
        Expression<Double> expr = num;
        
        assertEquals(Math.acos(0.5), unique(MathExpressions.acos(expr)), 0.001);
        assertEquals(Math.asin(0.5), unique(MathExpressions.asin(expr)), 0.001);
        assertEquals(Math.atan(0.5), unique(MathExpressions.atan(expr)), 0.001);
        assertEquals(Math.cos(0.5),  unique(MathExpressions.cos(expr)), 0.001);
        assertEquals(Math.cosh(0.5), unique(MathExpressions.cosh(expr)), 0.001);
        assertEquals(cot(0.5),       unique(MathExpressions.cot(expr)), 0.001);
        assertEquals(coth(0.5),      unique(MathExpressions.coth(expr)), 0.001);
        assertEquals(degrees(0.5),   unique(MathExpressions.degrees(expr)), 0.001);
        assertEquals(Math.exp(0.5),  unique(MathExpressions.exp(expr)), 0.001);
        assertEquals(Math.log(0.5),  unique(MathExpressions.ln(expr)), 0.001);
        assertEquals(log(0.5, 10),   unique(MathExpressions.log(expr, 10)), 0.001);
        assertEquals(0.25,           unique(MathExpressions.power(expr, 2)), 0.001);
        assertEquals(radians(0.5),   unique(MathExpressions.radians(expr)), 0.001);
        assertEquals(Integer.valueOf(1),              
                                     unique(MathExpressions.sign(expr)));
        assertEquals(Math.sin(0.5),  unique(MathExpressions.sin(expr)), 0.001);
        assertEquals(Math.sinh(0.5), unique(MathExpressions.sinh(expr)), 0.001);
        assertEquals(Math.tan(0.5),  unique(MathExpressions.tan(expr)), 0.001);
        assertEquals(Math.tanh(0.5), unique(MathExpressions.tanh(expr)), 0.001);
        
    }
    
    private double cot(double x) {
        return Math.cos(x) / Math.sin(x);
    }
    
    private double coth(double x) {
        return Math.cosh(x) / Math.sinh(x);
    }
    
    private double degrees(double x) {
        return x * 180.0 / Math.PI;
    }
    
    private double radians(double x) {
        return x * Math.PI / 180.0;
    }
    
    private double log(double x, int y) {
        return Math.log(x) / Math.log(y);
    }
        
    private <T> T unique(Expression<T> expr) {
        //return querydsl().uniqueResult(expr);
        return CollQueryFactory.from(num, Arrays.asList(0.5)).uniqueResult(expr);
    }

}
