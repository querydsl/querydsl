/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.util.TypeUtil;

/**
 * TypeModelFactory is a factory class for TypeModel instances
 * 
 * @author tiwe
 * 
 */
public class TypeModelFactory {

    private final Map<List<Type>, TypeModel> cache = new HashMap<List<Type>, TypeModel>();
    
    private final Collection<Class<? extends Annotation>> entityAnnotations;

    @SuppressWarnings("unchecked")
    public TypeModelFactory(Class<?>... entityAnnotations){
        this.entityAnnotations = (List)Arrays.asList(entityAnnotations);
    }
    
    public TypeModelFactory(List<Class<? extends Annotation>> entityAnnotations){
        this.entityAnnotations = entityAnnotations;
    }
    
    public TypeModel create(Class<?> cl){
        return create(cl, cl);
    }
    
    public TypeModel create(Class<?> cl, Type genericType) {
        List<Type> key = Arrays.<Type> asList(cl, genericType);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }else{
            if (cl.isPrimitive()) {
                cl = ClassUtils.primitiveToWrapper(cl);
            }
            TypeModel value;
            boolean entity= false;
            for (Class<? extends Annotation> clazz : entityAnnotations){
                if (cl.getAnnotation(clazz) != null){
                    entity = true;
                    break;
                }
            }            
            if (entity){
                value = new ClassTypeModel(TypeCategory.ENTITY, cl);
                
            }else if (cl.isArray()) {
                value = create(cl.getComponentType()).asArrayType();

            } else if (cl.isEnum()) {
                value = new ClassTypeModel(TypeCategory.SIMPLE, cl);

            } else if (Map.class.isAssignableFrom(cl)) {
                TypeModel keyInfo = create(TypeUtil.getTypeParameter(genericType, 0));
                TypeModel valueInfo = create(TypeUtil.getTypeParameter(genericType, 1));
                value = createMapType(keyInfo, valueInfo);

            } else if (List.class.isAssignableFrom(cl)) {
                TypeModel valueInfo = create(TypeUtil.getTypeParameter(genericType, 0));
                value = createListType(valueInfo);

            } else if (Set.class.isAssignableFrom(cl)) {
                TypeModel valueInfo = create(TypeUtil.getTypeParameter(genericType, 0));
                value = createSetType(valueInfo);
                
            } else if (Collection.class.isAssignableFrom(cl)) {
                TypeModel valueInfo = create(TypeUtil.getTypeParameter(genericType, 0));
                value = createCollectionType(valueInfo);
                
            }else if (Number.class.isAssignableFrom(cl) && Comparable.class.isAssignableFrom(cl)){    
                value = new ClassTypeModel(TypeCategory.NUMERIC, cl);
                
            } else {    
                TypeCategory typeCategory = TypeCategory.get(cl.getName());
                if (!typeCategory.isSubCategoryOf(TypeCategory.COMPARABLE) && Comparable.class.isAssignableFrom(cl)){
                    typeCategory = TypeCategory.COMPARABLE;
                }
                value = new ClassTypeModel(typeCategory, cl);
            }
            cache.put(key, value);
            return value;
        }
        
    }
    
    public TypeModel createCollectionType(TypeModel valueType) {
        return createComposite(TypeCategory.COLLECTION, Collection.class, valueType);
    }

    private TypeModel createComposite(TypeCategory container, Class<?> containerType, TypeModel... parameters) {
        return new SimpleTypeModel(container, 
                containerType.getName(), 
                containerType.getPackage().getName(), 
                containerType.getSimpleName(), 
                Modifier.isFinal(containerType.getModifiers()),
                parameters);

    }
    
    public TypeModel createListType(TypeModel valueType) {
        return createComposite(TypeCategory.LIST, List.class, valueType);
    }

    public TypeModel createMapType(TypeModel keyType, TypeModel valueType) {
        return createComposite(TypeCategory.MAP, Map.class, keyType, valueType);
    }

    public TypeModel createSetType(TypeModel valueType) {
        return createComposite(TypeCategory.SET, Collection.class, valueType);
    }

}
