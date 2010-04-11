/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.codegen.Evaluator;
import com.mysema.codegen.EvaluatorFactory;
import com.mysema.query.types.Expr;

/**
 * @author tiwe
 *
 */
@Immutable
public class ExprEvaluatorFactory {

    public static final ExprEvaluatorFactory DEFAULT = new ExprEvaluatorFactory(ColQueryTemplates.DEFAULT);
    
    static Object[] combine(int size, Object[]... arrays) {
        int offset = 0;
        Object[] target = new Object[size];
        for (Object[] arr : arrays) {
            System.arraycopy(arr, 0, target, offset, arr.length);
            offset += arr.length;
        }
        return target;
    }
    
    private final EvaluatorFactory factory;
    
    private final ColQueryTemplates templates;
    
    protected ExprEvaluatorFactory(ColQueryTemplates templates){
        this.templates = templates;
        // TODO : which ClassLoader to pick ?!?
        this.factory = new EvaluatorFactory((URLClassLoader)getClass().getClassLoader());
    }
    
    public <T> Evaluator<T> create(List<? extends Expr<?>> sources, final Expr<T> projection) {
        ColQuerySerializer serializer = new ColQuerySerializer(templates);
        serializer.handle(projection);
        Map<Object,String> constantToLabel = serializer.getConstantToLabel();
        String javaSource = serializer.toString();
        final Object[] constants = constantToLabel.keySet().toArray();        
        Class<?>[] types = new Class<?>[constants.length + sources.size()];
        String[] names = new String[constants.length + sources.size()];
        for (int i = 0; i < constants.length; i++) {
            if (List.class.isAssignableFrom(constants[i].getClass())){
                types[i] = List.class;
            }else if (Set.class.isAssignableFrom(constants[i].getClass())){
                types[i] = Set.class;
            }else if (Collection.class.isAssignableFrom(constants[i].getClass())){
                types[i] = Collection.class;
            }else{
                types[i] = constants[i].getClass();    
            }            
            names[i] = constantToLabel.get(constants[i]);
        }

        int off = constants.length;
        for (int i = 0; i < sources.size(); i++) {
            types[off + i] = sources.get(i).getType();
            names[off + i] = sources.get(i).toString();
        }
        
        // normalize types
        for (int i = 0; i < types.length; i++){
            if (ClassUtils.wrapperToPrimitive(types[i]) != null){
                types[i] = ClassUtils.wrapperToPrimitive(types[i]);
            }
        }

        final Evaluator<T> evaluator = factory.createEvaluator(javaSource, projection.getType(), names, types);
        if (constants.length > 0){
            return new Evaluator<T>(){
                @Override
                public T evaluate(Object... args) {
                    if (constants.length > 0){
                        args = combine(args.length + constants.length, constants, args);    
                    }                    
                    return evaluator.evaluate(args);
                }                
            };    
        }else{
            return evaluator;
        }
        
    }

    
}
