/**
 * 
 */
package com.mysema.query.collections.impl;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.janino.ExpressionEvaluator;

public final class JaninoEvaluator<T> implements Evaluator<T> {
    
    private final String javaSource;
    
    private final ExpressionEvaluator evaluator;
    
    private final String[] names;
    
    private final Object[] constArray;

    JaninoEvaluator(String javaSource, ExpressionEvaluator evaluator, String[] names, Object[] constArray) {
        this.javaSource = javaSource;
        this.evaluator = evaluator;
        this.names = names.clone();
        this.constArray = constArray.clone();
    }

    @Override
    public T evaluate(Object... args) {
        try {
            args = EvaluatorFactory.combine(constArray.length + args.length, constArray, args);
            return (T) evaluator.evaluate(args);
        } catch (InvocationTargetException e) {
            StringBuilder builder = new StringBuilder();
            builder.append("Caught exception when evaluating '").append(javaSource);
            builder.append("' with arguments ");
            for (int i = 0; i < args.length; i++){
                builder.append(names[i]).append(" = ").append(args[i]);
                if (i < args.length -1){
                    builder.append(", ");
                }
            }
            throw new RuntimeException(builder.toString(), e);
        }
    }
}