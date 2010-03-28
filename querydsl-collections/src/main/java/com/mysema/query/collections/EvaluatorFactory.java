/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.QueryException;
import com.mysema.query.types.Expr;

/**
 * @author tiwe
 *
 */
@Immutable
public class EvaluatorFactory {

    public static final EvaluatorFactory DEFAULT = new EvaluatorFactory(ColQueryTemplates.DEFAULT);
    
    private final ColQueryTemplates templates;
    
    protected EvaluatorFactory(ColQueryTemplates templates){
        this.templates = templates;
    }
    
    public <T> Evaluator<T> create(List<? extends Expr<?>> sources, final Expr<T> projection) {
        ColQuerySerializer serializer = new ColQuerySerializer(templates);
        serializer.handle(projection);
        Map<Object,String> constantToLabel = serializer.getConstantToLabel();
        String javaSource = serializer.toString();
        Object[] constArray = constantToLabel.keySet().toArray();        
        Class<?>[] types = new Class<?>[constArray.length + sources.size()];
        String[] names = new String[constArray.length + sources.size()];
        for (int i = 0; i < constArray.length; i++) {
            if (List.class.isAssignableFrom(constArray[i].getClass())){
                types[i] = List.class;
            }else if (Set.class.isAssignableFrom(constArray[i].getClass())){
                types[i] = Set.class;
            }else if (Collection.class.isAssignableFrom(constArray[i].getClass())){
                types[i] = Collection.class;
            }else{
                types[i] = constArray[i].getClass();    
            }            
            names[i] = constantToLabel.get(constArray[i]);
        }

        int off = constArray.length;
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

        try {
            return new SimpleEvaluator<T>(javaSource, projection.getType(), names, types, constArray);
        } catch (IOException e) {
            throw new QueryException(e);
        } catch (SecurityException e) {
            throw new QueryException(e);
        } catch (ClassNotFoundException e) {
            throw new QueryException(e);
        } catch (NoSuchMethodException e) {
            throw new QueryException(e);
        }
    }
    
    static Object[] combine(int size, Object[]... arrays) {
        int offset = 0;
        Object[] target = new Object[size];
        for (Object[] arr : arrays) {
            System.arraycopy(arr, 0, target, offset, arr.length);
            offset += arr.length;
        }
        return target;
    }

    
}
