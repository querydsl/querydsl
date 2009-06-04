package com.mysema.query;

import java.util.Arrays;
import java.util.Collection;

import com.mysema.query.functions.MathFunctions;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;

public abstract class TestExprs {
    
    // ENumber    
    
    public static Collection<ENumber<?>> getProjectionsForNumber(ENumber<Integer> expr, ENumber<Integer> other, int knownValue){
        return Arrays.<ENumber<?>>asList(
          MathFunctions.abs(expr),
          MathFunctions.add(expr, other),
          MathFunctions.div(expr, other),
          MathFunctions.max(expr, other),
          MathFunctions.min(expr, other),
          MathFunctions.mult(expr, other),
          MathFunctions.random(),
          MathFunctions.sqrt(expr),
          MathFunctions.sub(expr, other),
          expr.byteValue(),
          expr.doubleValue(),
          expr.floatValue(),
          expr.intValue(), 
          expr.longValue(),
          expr.shortValue()
//          path.stringValue()          
        );
    };
    
    // EString
    
    public static Collection<EString> getProjectionsForString(EString expr, EString other, String knownValue){
        return Arrays.<EString>asList(
          expr.add("Hello"),
          expr.add(other),
          expr.concat("Hello"),
          expr.concat(other),
          expr.lower(),
          expr.stringValue(),
          expr.substring(1),
          expr.substring(0, 1),
          expr.trim(),
          expr.upper()
        );
    }
    
    private TestExprs(){}

}
