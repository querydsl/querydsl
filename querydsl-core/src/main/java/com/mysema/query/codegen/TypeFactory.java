/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.Types;
import com.mysema.util.ReflectionUtils;

/**
 * TypeFactory is a factory class for Type instances
 *
 * @author tiwe
 *
 */
public final class TypeFactory {

    private final Map<List<java.lang.reflect.Type>, Type> cache = new HashMap<List<java.lang.reflect.Type>, Type>();

    private final Collection<Class<? extends Annotation>> entityAnnotations;

    private final Set<Class<?>> embeddableTypes = new HashSet<Class<?>>();

    private boolean unknownAsEntity;

    @SuppressWarnings("unchecked")
    public TypeFactory(Class<?>... entityAnnotations){
        this((List)Arrays.asList(entityAnnotations));
    }

    public TypeFactory(List<Class<? extends Annotation>> entityAnnotations){
        this.entityAnnotations = entityAnnotations;
    }

    public Type create(Class<?> cl){
        return create(cl, cl);
    }

    public Type create(Class<?> cl, java.lang.reflect.Type genericType) {
        List<java.lang.reflect.Type> key = Arrays.<java.lang.reflect.Type> asList(cl, genericType);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }else{
            if (cl.isPrimitive()) {
                cl = ClassUtils.primitiveToWrapper(cl);
            }
            Type value;
            boolean entity= false;
            for (Class<? extends Annotation> clazz : entityAnnotations){
                if (cl.getAnnotation(clazz) != null){
                    entity = true;
                    break;
                }
            }
            if (embeddableTypes.contains(cl)) {
                entity = true;
            }

            Type[] parameters = getParameters(cl, genericType,
                    (Collection.class.isAssignableFrom(cl) || Map.class.isAssignableFrom(cl)));

            if (cl.isArray()) {
                value = create(cl.getComponentType()).asArrayType();

            } else if (cl.isEnum()) {
                value = new ClassType(TypeCategory.ENUM, cl);

            } else if (Map.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.MAP, parameters[0], parameters[1]);

            } else if (List.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.LIST, parameters[0]);

            } else if (Set.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.SET, parameters[0]);

            } else if (Collection.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.COLLECTION, parameters[0]);

            }else if (Number.class.isAssignableFrom(cl) && Comparable.class.isAssignableFrom(cl)){
                value = new ClassType(TypeCategory.NUMERIC, cl, parameters);

            } else {
                TypeCategory typeCategory = TypeCategory.get(cl.getName());
                if (!typeCategory.isSubCategoryOf(TypeCategory.COMPARABLE) && Comparable.class.isAssignableFrom(cl)){
                    typeCategory = TypeCategory.COMPARABLE;
                }else if (entity){
                    typeCategory = TypeCategory.ENTITY;
                }else if (unknownAsEntity && typeCategory == TypeCategory.SIMPLE && !cl.getName().startsWith("java")){
                    typeCategory = TypeCategory.ENTITY;
                }
                value = new ClassType(typeCategory, cl, parameters);
            }

            if (entity){
                value = new EntityType(value);
            }

            cache.put(key, value);
            return value;
        }

    }

    private Type[] getParameters(Class<?> cl, java.lang.reflect.Type genericType, boolean collectionOrMap) {
        int parameterCount = ReflectionUtils.getTypeParameterCount(genericType);
        if (parameterCount > 0){
            return getGenericParameters(genericType, collectionOrMap, parameterCount);

        } else if (Collection.class.isAssignableFrom(cl)) {
            return new Type[]{ Types.OBJECT }; // TODO : cache

        } else if (Map.class.isAssignableFrom(cl)) {
            return new Type[]{ Types.OBJECT, Types.OBJECT }; // TODO : cache

        } else if (cl.getTypeParameters().length > 0) {
            return getTypeParameters(cl);

        } else {
            return new Type[0]; // TODO : cache
        }

    }

    private Type[] getGenericParameters(java.lang.reflect.Type genericType, boolean collectionOrMap, int parameterCount) {
        Type[] types = new Type[parameterCount];
        for (int i = 0; i < types.length; i++){
            java.lang.reflect.Type parameter = ((ParameterizedType)genericType).getActualTypeArguments()[i];
            if (parameter instanceof WildcardType && !collectionOrMap){
                types[i] = null;
            }else{
                types[i] = create(ReflectionUtils.getTypeParameter(genericType, i), parameter);
                if (parameter instanceof WildcardType){
                    types[i] = new TypeExtends(types[i]);
                }
            }
        }
        return types;
    }

    private Type[] getTypeParameters(Class<?> cl) {
        Type[] types = new Type[cl.getTypeParameters().length];
        for (int i = 0; i < types.length; i++) {
            TypeVariable<?> typeVariable = cl.getTypeParameters()[i];
            if (!typeVariable.getGenericDeclaration().equals(cl)){
                types[i] = create((Class<?>)typeVariable.getGenericDeclaration(), typeVariable);
            }else{
                types[i] = new ClassType(cl);
            }
        }
        return types;
    }

    public void setUnknownAsEntity(boolean unknownAsEntity) {
        this.unknownAsEntity = unknownAsEntity;
    }

    public void addEmbeddableType(Class<?> cl) {
        embeddableTypes.add(cl);
    }

}
