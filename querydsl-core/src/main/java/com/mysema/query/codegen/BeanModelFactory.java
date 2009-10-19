/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.jcip.annotations.Immutable;

import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.annotations.QueryType;

/**
 * A Reflection based Factory implementation for ClassModel instance
 * 
 * @author tiwe
 *
 */
@Immutable
public class BeanModelFactory {
    
    private final TypeModelFactory typeModelFactory;
    
    private final Class<? extends Annotation> skipAnn;
    
    public BeanModelFactory(TypeModelFactory typeModelFactory, Class<? extends Annotation> skipAnn){
        this.typeModelFactory = typeModelFactory;
        this.skipAnn = skipAnn;
    }
    
    public BeanModelFactory(TypeModelFactory typeModelFactory){
        this(typeModelFactory, QueryTransient.class);
    }
    
    public BeanModel create(Class<?> key, String prefix){
        Collection<String> superTypes;
        if (key.isInterface()){
            superTypes = new ArrayList<String>();
            for (Class<?> iface : key.getInterfaces()){
                if (!iface.getName().startsWith("java")){
                    superTypes.add(iface.getName());
                }
            }
        }else{
            superTypes = Collections.singleton(key.getSuperclass().getName());
        }
        BeanModel beanModel = new BeanModel(
                prefix, 
                key.getPackage().getName(), 
                key.getName(), 
                key.getSimpleName(),
                superTypes);
        for (Field f : key.getDeclaredFields()) {
            if (isValidField(f)){
                TypeModel typeModel = typeModelFactory.create(f.getType(), f.getGenericType());
                if (f.getAnnotation(QueryType.class) != null){
                    TypeCategory typeCategory = TypeCategory.get(f.getAnnotation(QueryType.class).value());
                    if (typeCategory == null){
                        continue;
                    }
                    typeModel = typeModel.as(typeCategory);
                }
                beanModel.addProperty(new PropertyModel(beanModel, f.getName(), typeModel));    
            }            
        }
        return beanModel;
    }

    protected boolean isValidField(Field field) {
        return field.getAnnotation(skipAnn) == null
            && !Modifier.isTransient(field.getModifiers()) 
            && !Modifier.isStatic(field.getModifiers());
    }
    
}
