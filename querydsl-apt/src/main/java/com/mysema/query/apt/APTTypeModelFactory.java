/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import com.mysema.query.codegen.EntityModel;
import com.mysema.query.codegen.SimpleTypeModel;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeExtendsModel;
import com.mysema.query.codegen.TypeModel;
import com.mysema.query.codegen.TypeModelFactory;
import com.mysema.query.codegen.TypeModels;
import com.mysema.query.codegen.TypeSuperModel;

/**
 * APTTypeModelFactory is a factory for APT inspection based TypeModel creation
 * 
 * @author tiwe
 *
 */
public class APTTypeModelFactory {
    
    private final Map<List<String>,TypeModel> cache = new HashMap<List<String>,TypeModel>();

    private final Configuration configuration;
    
    private final TypeModel defaultValue;
    
    private final List<Class<? extends Annotation>> entityAnnotations;
    
    private final Map<List<String>,EntityModel> entityTypeCache = new HashMap<List<String>,EntityModel>();
    
    private final ProcessingEnvironment env;
    
    private final TypeModelFactory factory;
    
    private final TypeElement numberType, comparableType;
    
    public APTTypeModelFactory(ProcessingEnvironment env, Configuration configuration, 
            TypeModelFactory factory, List<Class<? extends Annotation>> annotations){
        this.env = env;
        this.configuration = configuration;
        this.factory = factory;
        this.defaultValue = factory.create(Object.class);       
        this.entityAnnotations = annotations;
        this.numberType = env.getElementUtils().getTypeElement(Number.class.getName());
        this.comparableType = env.getElementUtils().getTypeElement(Comparable.class.getName());        
    }
    
    private EntityModel asEntityModel(TypeMirror type, TypeModel value) {                 
        if (type.getKind() == TypeKind.TYPEVAR){
            TypeVariable typeVar = (TypeVariable)type;
            if (typeVar.getUpperBound() != null){
                type = typeVar.getUpperBound();
            }
        }else if (type.getKind() == TypeKind.WILDCARD){
            WildcardType wildcard = (WildcardType)type;
            if (wildcard.getExtendsBound() != null){
                type = wildcard.getExtendsBound();
            }
        }
        
        Collection<String> superTypes = Collections.emptySet();
        if (type.getKind() == TypeKind.DECLARED){
            DeclaredType declaredType = (DeclaredType)type;
            TypeElement e = (TypeElement)declaredType.asElement();
            if (e.getKind() == ElementKind.CLASS){
                superTypes = Collections.singleton(create(e.getSuperclass()).getFullName());
            }else{
                superTypes = new ArrayList<String>(e.getInterfaces().size());
                for (TypeMirror mirror : e.getInterfaces()){
                    TypeModel iface = create(mirror);
                    if (!iface.getFullName().startsWith("java")){
                        superTypes.add(iface.getFullName());
                    }
                }
            }
            
        }else{
            throw new IllegalArgumentException("Unsupported type kind " + type.getKind());
        }
        
        return new EntityModel(configuration.getNamePrefix(), value, superTypes);
    }
    
    private TypeModel create(TypeElement typeElement, TypeCategory category, 
            List<? extends TypeMirror> typeArgs) {
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        String packageName = env.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        TypeModel[] params = new TypeModel[typeArgs.size()];
        for (int i = 0; i < params.length; i++){
            params[i] = create(typeArgs.get(i));
        }        
        return new SimpleTypeModel(category, 
            name, packageName, simpleName, 
            typeElement.getModifiers().contains(Modifier.FINAL), 
            params);
    }
    
    /**
     * Create a TypeModel for the given TypeMirror
     * 
     * @param type
     * @param el
     * @return
     */
    @Nullable
    public TypeModel create(TypeMirror type){
        List<String> key = getKey(type, true);  
        if (entityTypeCache.containsKey(key)){
            return entityTypeCache.get(key);
            
        }else if (cache.containsKey(key)){
            return cache.get(key);
            
        }else{
            cache.put(key, null);
            TypeModel value = handle(type);
            if (value != null && value.getCategory() == TypeCategory.ENTITY){                
                value = asEntityModel(type, value);
                entityTypeCache.put(key, (EntityModel)value);
            }else{
                cache.put(key, value);    
            }            
            return value;
        }        
    }

    private TypeModel createClassType(DeclaredType t, TypeElement typeElement) {   
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY,  t.getTypeArguments());
            }
        }        
        
        // other
        String name = typeElement.getQualifiedName().toString();
        TypeCategory typeCategory = TypeCategory.get(name);
        
        if (typeCategory != TypeCategory.NUMERIC
                && isAssignable(typeElement, comparableType)
                && isSubType(typeElement, numberType)){
            typeCategory = TypeCategory.NUMERIC;
            
        }else if (!typeCategory.isSubCategoryOf(TypeCategory.COMPARABLE)
                && isAssignable(typeElement, comparableType)){
            typeCategory = TypeCategory.COMPARABLE;
        }
        return create(typeElement, typeCategory, t.getTypeArguments());
    }

    /**
     * Create an EntityModel for the given TypeMirror
     * 
     * @param type
     * @param el
     * @return
     */
    @Nullable
    public EntityModel createEntityModel(TypeMirror type){
        List<String> key = getKey(type, true);
        if (entityTypeCache.containsKey(key)){
            return entityTypeCache.get(key);
        
        }else{
            entityTypeCache.put(key, null);
            TypeModel value = handle(type);
            if (value != null){                
                EntityModel entityModel = asEntityModel(type, value);
                entityTypeCache.put(key, entityModel);
                return entityModel;
            }else{
                return null;
            }
        }
    }

    private TypeModel createEnumType(DeclaredType t, TypeElement typeElement) {
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, t.getTypeArguments());
            }
        }  
        
        // fallback
        return create(typeElement, TypeCategory.SIMPLE, t.getTypeArguments());
    }
    
    @Nullable
    private static Class<?> safeForName(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    private TypeModel createInterfaceType(DeclaredType t, TypeElement typeElement) {
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, t.getTypeArguments());
            }
        }       
                
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        Iterator<? extends TypeMirror> i = t.getTypeArguments().iterator();
        Class<?> cl = safeForName(name);
        if (cl == null) { // class not available
            return create(typeElement, TypeCategory.get(name), t.getTypeArguments());
            
        }else if (Map.class.isAssignableFrom(cl)){
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createMapType(create(i.next()), create(i.next()));

        } else if (List.class.isAssignableFrom(cl)) {
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createListType(create(i.next()));
            
        } else if (Set.class.isAssignableFrom(cl)) {
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createSetType(create(i.next()));
            
            
        } else if (Collection.class.isAssignableFrom(cl)) {
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createCollectionType(create(i.next()));
        
        }else{
            return create(typeElement, TypeCategory.get(name), t.getTypeArguments());
        }
    }

    /**
     * Create a cache key for the given TypeMirror
     * 
     * @param type
     * @param deep inspect type arguments
     * @return
     */
    private List<String> getKey(TypeMirror type, boolean deep){
        List<String> key = new ArrayList<String>();
        key.add(type.toString());
        if (type.getKind() == TypeKind.TYPEVAR){
            TypeVariable t = (TypeVariable)type;
            if (t.getUpperBound() != null){
                key.addAll(getKey(t.getUpperBound(), false));
            }            
            if (t.getLowerBound() != null){
                key.addAll(getKey(t.getLowerBound(), false));
            }
        }else if (type.getKind() == TypeKind.WILDCARD){
            WildcardType t = (WildcardType)type;
            if (t.getExtendsBound() != null){
                key.addAll(getKey(t.getExtendsBound(), false));
            }
            if (t.getSuperBound() != null){
                key.addAll(getKey(t.getSuperBound(), false));
            }
        }else if (type.getKind() == TypeKind.DECLARED){
            DeclaredType t = (DeclaredType)type;
            for (TypeMirror arg : t.getTypeArguments()){
                key.addAll(deep ? getKey(arg, false) : Collections.singleton(arg.toString()));
            }
        }
        return key;
    }

    @Nullable
    private TypeModel handle(TypeMirror type) {
        if (type instanceof DeclaredType){
            DeclaredType t = (DeclaredType)type;
            if (t.asElement() instanceof TypeElement){
                TypeElement typeElement = (TypeElement)t.asElement();
                switch(typeElement.getKind()){
                case ENUM:      return createEnumType(t, typeElement);
                case CLASS:     return createClassType(t, typeElement);
                case INTERFACE: return createInterfaceType(t, typeElement);
                default: throw new IllegalArgumentException("Illegal type " + typeElement);
                }            
            }else{
                throw new IllegalArgumentException("Unsupported element type " + t.asElement());
            }
            
        }else if (type instanceof TypeVariable){
            TypeVariable t = (TypeVariable)type;
            String varName = t.toString();
            if (t.getUpperBound() != null){
                return new TypeExtendsModel(varName, handle(t.getUpperBound()));
            }else if (t.getLowerBound() != null && !(t.getLowerBound() instanceof NullType)){
                return new TypeSuperModel(varName, handle(t.getLowerBound()));
            }else{
                return null;
            }              
            
        }else if (type instanceof WildcardType){
            WildcardType t = (WildcardType)type;
            if (t.getExtendsBound() != null){
                return new TypeExtendsModel(handle(t.getExtendsBound()));
            }else if (t.getSuperBound() != null){
                return new TypeSuperModel(handle(t.getSuperBound()));
            }else{            
                return null;
            }
            
        }else if (type instanceof ArrayType){
            ArrayType t = (ArrayType)type;
            return create(t.getComponentType()).asArrayType();
            
        }else if (type instanceof PrimitiveType){
            PrimitiveType t = (PrimitiveType)type;
            switch (t.getKind()) {
            case BOOLEAN: return TypeModels.BOOLEAN;
            case BYTE: return TypeModels.BYTE;
            case CHAR: return TypeModels.CHAR;
            case DOUBLE: return TypeModels.DOUBLE;
            case FLOAT: return TypeModels.FLOAT;
            case INT: return TypeModels.INT;
            case LONG: return TypeModels.LONG;
            case SHORT: return TypeModels.SHORT;
            }
            throw new IllegalArgumentException("Unsupported type " + t.getKind());

            
        }else if (type instanceof NoType){
            return defaultValue;

        }else{
            return null;    
        }        
    }

    private boolean isAssignable(TypeElement type1, TypeElement type2) {
        TypeMirror t1 = type1.asType();
        TypeMirror t2 = env.getTypeUtils().erasure(type2.asType());
        return env.getTypeUtils().isAssignable(t1, t2);
    }

    
    private boolean isSubType(TypeElement type1, TypeElement type2) {
        return env.getTypeUtils().isSubtype(type1.asType(), type2.asType());
    }

    
}
