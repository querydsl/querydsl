/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.codegen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
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

import com.google.common.primitives.Primitives;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.TypeSuper;
import com.mysema.codegen.model.Types;
import com.mysema.util.ReflectionUtils;

/**
 * TypeFactory is a factory class for {@link Type} instances
 *
 * @author tiwe
 *
 */
// TODO : refactor this to be more understandable
public final class TypeFactory {

    private static final Type ANY = new TypeExtends(Types.OBJECT);
    
    private static final Type[] TYPES_0 = new Type[0];

    private static final Type[] TYPES_1 = new Type[]{ Types.OBJECT };
    
    private static final Type[] TYPES_2 = new Type[]{ Types.OBJECT, Types.OBJECT };

    private final Map<List<java.lang.reflect.Type>, Type> cache = new HashMap<List<java.lang.reflect.Type>, Type>();
    
    private final Collection<Class<? extends Annotation>> entityAnnotations;

    private final Set<Class<?>> embeddableTypes = new HashSet<Class<?>>();

    private boolean unknownAsEntity = false;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TypeFactory(Class<?>... entityAnnotations) {
        this((List)Arrays.asList(entityAnnotations));
    }

    public TypeFactory(List<Class<? extends Annotation>> entityAnnotations) {
        this.entityAnnotations = entityAnnotations;
    }

    public EntityType createEntityType(Class<?> cl) {
        return (EntityType) create(true, cl, cl);
    }
    
    public Type create(Class<?> cl) {
        return create(isEntityClass(cl), cl, cl);
    }

    public Type create(Class<?> cl, java.lang.reflect.Type genericType) {
        return create(isEntityClass(cl), cl, genericType);
    }
    
    public Type create(boolean entity, Class<?> cl, java.lang.reflect.Type genericType) {        
        List<java.lang.reflect.Type> key = Arrays.<java.lang.reflect.Type> asList(cl, genericType);
        if (cache.containsKey(key)) {
            Type value = cache.get(key);
            if (entity && !(value instanceof EntityType)) {
                value = new EntityType(value);
                cache.put(key, value);
            }
            return value;
            
        } else {
            if (cl.isPrimitive()) {
                cl = Primitives.wrap(cl);
            }
            Type value;
            Type[] tempParams = (Type[]) Array.newInstance(Type.class, 
                    ReflectionUtils.getTypeParameterCount(genericType));
            cache.put(key, new ClassType(cl, tempParams));
            Type[] parameters = getParameters(cl, genericType);

            if (cl.isArray()) {
                value = create(cl.getComponentType()).asArrayType();
            } else if (cl.isEnum()) {
                value = new ClassType(TypeCategory.ENUM, cl);
            } else if (Number.class.isAssignableFrom(cl) && Comparable.class.isAssignableFrom(cl)) {
                value = new ClassType(TypeCategory.NUMERIC, cl, parameters);
            } else if (entity) {
                value = createOther(cl, entity, parameters);
            } else if (Map.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.MAP, parameters[0], parameters[1]);
            } else if (List.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.LIST, parameters[0]);
            } else if (Set.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.SET, parameters[0]);
            } else if (Collection.class.isAssignableFrom(cl)) {
                value = new SimpleType(Types.COLLECTION, parameters[0]);            
            } else {
                value = createOther(cl, entity, parameters);
            }
            
            if (genericType instanceof TypeVariable) {
                TypeVariable tv = (TypeVariable)genericType;
                if (tv.getBounds().length == 1 && tv.getBounds()[0].equals(Object.class)) {
                    value = new TypeSuper(tv.getName(), value);
                } else {
                    value = new TypeExtends(tv.getName(), value);
                }
            } 

            if (entity) {
                value = new EntityType(value);
            }

            cache.put(key, value);
            return value;
        }

    }

    private Type createOther(Class<?> cl, boolean entity, Type[] parameters) {
        Type value;
        TypeCategory typeCategory = TypeCategory.get(cl.getName());
        if (!typeCategory.isSubCategoryOf(TypeCategory.COMPARABLE) && Comparable.class.isAssignableFrom(cl)
            && !cl.equals(Comparable.class)) {
            typeCategory = TypeCategory.COMPARABLE;
        } else if (embeddableTypes.contains(cl)) {
            typeCategory = TypeCategory.CUSTOM;
        } else if (typeCategory == TypeCategory.SIMPLE && entity) {
            typeCategory = TypeCategory.ENTITY;
        } else if (unknownAsEntity && typeCategory == TypeCategory.SIMPLE && !cl.getName().startsWith("java")) {
            typeCategory = TypeCategory.CUSTOM;
        }
        value = new ClassType(typeCategory, cl, parameters);
        return value;
    }

    private Type[] getParameters(Class<?> cl, java.lang.reflect.Type genericType) {
        int parameterCount = ReflectionUtils.getTypeParameterCount(genericType);
        if (parameterCount > 0) {
            boolean collectionOrMap = Collection.class.isAssignableFrom(cl) || Map.class.isAssignableFrom(cl);
            return getGenericParameters(cl, genericType, collectionOrMap, parameterCount);
        } else if (Collection.class.isAssignableFrom(cl)) {
            return TYPES_1; 
        } else if (Map.class.isAssignableFrom(cl)) {
            return TYPES_2; 
        } else if (cl.getTypeParameters().length > 0) {
            return getTypeParameters(cl);
        } else {
            return TYPES_0; 
        }
    }

    private Type[] getGenericParameters(Class<?> cl, java.lang.reflect.Type genericType,
            boolean collectionOrMap, int parameterCount) {
        Type[] types = new Type[parameterCount];
        for (int i = 0; i < types.length; i++) {
            types[i] = getGenericParameter(cl, genericType, collectionOrMap, i);            
        }
        return types;
    }

    @SuppressWarnings("rawtypes")
    private Type getGenericParameter(Class<?> cl, java.lang.reflect.Type genericType,
            boolean collectionOrMap, int i) {
        java.lang.reflect.Type parameter = ((ParameterizedType)genericType).getActualTypeArguments()[i];
        if (parameter instanceof TypeVariable
            && ((TypeVariable)parameter).getBounds()[0].equals(Object.class)) {
            return collectionOrMap ? Types.OBJECT : null;
        } else if (parameter instanceof WildcardType 
            && ((WildcardType)parameter).getUpperBounds()[0].equals(Object.class)
            && ((WildcardType)parameter).getLowerBounds().length == 0) {
            Type rv = getTypeParameter(cl, i);
            return (collectionOrMap && rv == null) ? ANY : rv;
        } else {
            Type rv = create(ReflectionUtils.getTypeParameter(genericType, i), parameter);
            if (parameter instanceof WildcardType) {
                rv = new TypeExtends(rv);
            } 
            return rv;
        }
    }

    private Type[] getTypeParameters(Class<?> cl) {
        Type[] types = new Type[cl.getTypeParameters().length];
        for (int i = 0; i < types.length; i++) {
            types[i] = getTypeParameter(cl, i);
        }
        return types;
    }
    
    @SuppressWarnings("rawtypes")
    private Type getTypeParameter(Class<?> cl, int i) {
        TypeVariable<?> typeVariable = cl.getTypeParameters()[i];
        java.lang.reflect.Type firstBound = typeVariable.getBounds()[0];
        if (firstBound.equals(Object.class)) {
            return null;
        } else if (firstBound.equals(cl)) {
            return new ClassType(cl);                
        } else if (firstBound instanceof Class) {
            return create((Class)typeVariable.getBounds()[0], typeVariable);    
        } else  if (firstBound instanceof ParameterizedType){
            ParameterizedType parameterized = (ParameterizedType)firstBound;
            Class<?> rawType = (Class)parameterized.getRawType();
            if (rawType.equals(cl)) {
                return new TypeExtends(typeVariable.getName(), new ClassType(cl));
            } else {                
                return new TypeExtends(create(rawType, parameterized));
            }                
        } else {
            throw new IllegalStateException(typeVariable.getBounds()[0].getClass().toString());
        }
    }


    private boolean isEntityClass(Class<?> cl) {
        for (Class<? extends Annotation> clazz : entityAnnotations){
            if (cl.getAnnotation(clazz) != null){
                return true;
            }
        }
        return embeddableTypes.contains(cl);
    }
    
    public void setUnknownAsEntity(boolean unknownAsEntity) {
        this.unknownAsEntity = unknownAsEntity;
    }

    public void addEmbeddableType(Class<?> cl) {
        embeddableTypes.add(cl);
    }

}
