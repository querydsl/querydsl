/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

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
    
    public ExprEvaluatorFactory(ColQueryTemplates templates){
        // TODO : which ClassLoader to pick ?!?
	this(templates, (URLClassLoader)ExprEvaluatorFactory.class.getClassLoader(), ToolProvider.getSystemJavaCompiler());
    }
    
    public ExprEvaluatorFactory(ColQueryTemplates templates, URLClassLoader classLoader, JavaCompiler compiler){
        this.templates = templates;
        this.factory = new EvaluatorFactory(classLoader, compiler);
    }
    
    public <T> Evaluator<T> create(List<? extends Expr<?>> sources, final Expr<T> projection) {
        ColQuerySerializer serializer = new ColQuerySerializer(templates);
        serializer.handle(projection);
        Map<Object,String> constantToLabel = serializer.getConstantToLabel();
        Map<String,Object> constants = new HashMap<String,Object>();
        for (Map.Entry<Object,String> entry : constantToLabel.entrySet()){
            constants.put(entry.getValue(), entry.getKey());
        }
        String javaSource = serializer.toString();        
        Class<?>[] types = new Class<?>[sources.size()];
        String[] names = new String[sources.size()];
        for (int i = 0; i < sources.size(); i++) {
            types[i] = sources.get(i).getType();
            names[i] = sources.get(i).toString();
        }
        
        // normalize types
        for (int i = 0; i < types.length; i++){
            if (ClassUtils.wrapperToPrimitive(types[i]) != null){
                types[i] = ClassUtils.wrapperToPrimitive(types[i]);
            }
        }
        return factory.createEvaluator(javaSource, projection.getType(), names, types, constants);        
    }

    
}
