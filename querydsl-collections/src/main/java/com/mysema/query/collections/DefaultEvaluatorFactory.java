/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.codegen.Evaluator;
import com.mysema.codegen.EvaluatorFactory;
import com.mysema.codegen.Type;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.expr.EBoolean;

/**
 * DefaultEvaluatorFactory extends the EvaluatorFactory class to provide Java source 
 * templates for evaluation of ColQuery queries
 * 
 * @author tiwe
 *
 */
@Immutable
public class DefaultEvaluatorFactory {
    
    private final EvaluatorFactory factory;
    
    private final ColQueryTemplates templates;
    
    public DefaultEvaluatorFactory(ColQueryTemplates templates){
        // TODO : which ClassLoader to pick ?!?
	this(templates, 
	    (URLClassLoader)DefaultEvaluatorFactory.class.getClassLoader(), 
	    ToolProvider.getSystemJavaCompiler());
    }
    
    public DefaultEvaluatorFactory(ColQueryTemplates templates, 
            URLClassLoader classLoader, JavaCompiler compiler){
        this.templates = templates;
        this.factory = new EvaluatorFactory(classLoader, compiler);
    }
    
    /**
     * Create an Evaluator for the given query sources and projection
     * 
     * @param <T>
     * @param sources
     * @param projection
     * @return
     */
    public <T> Evaluator<T> create(List<? extends Expr<?>> sources, Expr<T> projection) {
        ColQuerySerializer serializer = new ColQuerySerializer(templates);
        serializer.handle(projection);
        
        Map<Object,String> constantToLabel = serializer.getConstantToLabel();
        Map<String,Object> constants = new HashMap<String,Object>();
        for (Map.Entry<Object,String> entry : constantToLabel.entrySet()){
            constants.put(entry.getValue(), entry.getKey());
        }
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
        
        String javaSource = serializer.toString();
        if (projection instanceof EConstructor<?>){
            javaSource = "("+com.mysema.codegen.ClassUtils.getName(projection.getType())+")(" + javaSource+")";
        }
        
        return factory.createEvaluator("return " + javaSource +";", projection.getType(), names, types, constants);        
    }
    
    /**
     * Create an Evaluator for the given source and filter
     * 
     * @param <T>
     * @param source
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Evaluator<List<T>> createEvaluator(Expr<? extends T> source, EBoolean filter){
        String typeName = com.mysema.codegen.ClassUtils.getName(source.getType());
        ColQuerySerializer ser = new ColQuerySerializer(templates);
        ser.append("java.util.List<"+typeName+"> rv = new java.util.ArrayList<"+typeName+">();\n");
        ser.append("for (" + typeName + " "+ source + " : " + source + "_){\n");
        ser.append("    if (").handle(filter).append("){\n");
        ser.append("        rv.add("+source+");\n");
        ser.append("    }\n");
        ser.append("}\n");
        ser.append("return rv;");
        
        Map<Object,String> constantToLabel = ser.getConstantToLabel();
        Map<String,Object> constants = new HashMap<String,Object>();
        for (Map.Entry<Object,String> entry : constantToLabel.entrySet()){
            constants.put(entry.getValue(), entry.getKey());
        }
        
        Type sourceType = new Type(source.getType());
        Type sourceListType = new Type(Iterable.class, sourceType); 
        
        return factory.createEvaluator(
                ser.toString(), 
                sourceListType, 
                new String[]{source+"_"}, 
                new Type[]{sourceListType}, 
                new Class[]{Iterable.class}, 
                constants);
    }
    
    /**
     * Create an Evaluator for the given sources and the given optional filter
     * 
     * @param joins
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public Evaluator<List<Object[]>> createEvaluator(List<JoinExpression> joins, @Nullable EBoolean filter){
        List<String> sourceNames = new ArrayList<String>();
        List<Type> sourceTypes = new ArrayList<Type>();
        List<Class> sourceClasses = new ArrayList<Class>();
        StringBuilder vars = new StringBuilder();
        ColQuerySerializer ser = new ColQuerySerializer(templates);        
        ser.append("java.util.List<Object[]> rv = new java.util.ArrayList<Object[]>();\n");
        
        // creating context
        for (JoinExpression join : joins){
            Expr<?> target = join.getTarget();
            String typeName = com.mysema.codegen.ClassUtils.getName(target.getType());
            if (vars.length() > 0){
                vars.append(",");
            }
            if (join.getType() == JoinType.DEFAULT){
                ser.append("for (" + typeName + " "+ target + " : " + target + "_){\n");
                vars.append(target);
                sourceNames.add(target+"_");
                sourceTypes.add(new Type(Iterable.class, new Type(target.getType())));
                sourceClasses.add(Iterable.class);
                
            }else if (join.getType() == JoinType.INNERJOIN){
                Operation alias = (Operation)join.getTarget();
                // TODO : handle also Map inner joins
                // TODO : handle join condition
                ser.append("for ( " + typeName + " " + alias.getArg(1) + " : ").handle(alias.getArg(0)).append("){\n");
                vars.append(alias.getArg(1));
                
            // TODO : left join    
                
            }else{
                throw new IllegalArgumentException("Illegal join expression " + join);
            }
        }
        
        // filter
        if (filter != null){
            ser.append("if (").handle(filter).append("){\n");
            ser.append("    rv.add(new Object[]{"+vars+"});\n");
            ser.append("}\n");    
        }else{
            ser.append("rv.add(new Object[]{"+vars+"});\n");
        }
        
        // closing context
        for (int i = 0; i < joins.size(); i++){
            ser.append("}\n");    
        }        
        ser.append("return rv;");
        
        Map<Object,String> constantToLabel = ser.getConstantToLabel();
        Map<String,Object> constants = new HashMap<String,Object>();
        for (Map.Entry<Object,String> entry : constantToLabel.entrySet()){
            constants.put(entry.getValue(), entry.getKey());
        }
        
        Type projectionType = new Type(List.class, new Type(Object[].class));        
        return factory.createEvaluator(
                ser.toString(), 
                projectionType, 
                sourceNames.toArray(new String[sourceNames.size()]), 
                sourceTypes.toArray(new Type[sourceTypes.size()]), 
                sourceClasses.toArray(new Class[sourceClasses.size()]), 
                constants);
    }

    
}
