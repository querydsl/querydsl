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
import java.util.HashSet;
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

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.TypeSuper;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Supertype;
import com.mysema.query.codegen.TypeFactory;

/**
 * APTTypeFactory is a factory for APT inspection based TypeModel creation
 *
 * @author tiwe
 *
 */
// TODO : improved entityTypeCache and cache usage
public final class APTTypeFactory {

    @Nullable
    private static Class<?> safeClassForName(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private final Map<List<String>,Type> cache = new HashMap<List<String>,Type>();

    private final Configuration configuration;

    private final Type defaultValue;

    private final List<Class<? extends Annotation>> entityAnnotations;

    private final Map<List<String>,EntityType> entityTypeCache = new HashMap<List<String>,EntityType>();

    private final ProcessingEnvironment env;

    private final TypeElement numberType, comparableType;

    private boolean doubleIndexEntities = true;

    public APTTypeFactory(ProcessingEnvironment env, Configuration configuration,
            TypeFactory factory, List<Class<? extends Annotation>> annotations){
        this.env = env;
        this.configuration = configuration;
        this.defaultValue = factory.create(Object.class);
        this.entityAnnotations = annotations;
        this.numberType = env.getElementUtils().getTypeElement(Number.class.getName());
        this.comparableType = env.getElementUtils().getTypeElement(Comparable.class.getName());
    }

    private void appendToKey(List<String> key, DeclaredType t, boolean deep) {
        for (TypeMirror arg : t.getTypeArguments()){
            if (deep){
                key.addAll(createKey(arg, false));
            }else{
                key.add(arg.toString());
            }
        }
    }

    private void appendToKey(List<String> key, TypeVariable t) {
        if (t.getUpperBound() != null){
            key.addAll(createKey(t.getUpperBound(), false));
        }
        if (t.getLowerBound() != null){
            key.addAll(createKey(t.getLowerBound(), false));
        }
    }

    private void appendToKey(List<String> key, WildcardType t) {
        if (t.getExtendsBound() != null){
            key.addAll(createKey(t.getExtendsBound(), false));
        }
        if (t.getSuperBound() != null){
            key.addAll(createKey(t.getSuperBound(), false));
        }
    }

    private Type create(TypeElement typeElement, TypeCategory category, List<? extends TypeMirror> typeArgs) {
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        String packageName = env.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        Type[] params = new Type[typeArgs.size()];
        for (int i = 0; i < params.length; i++){
            params[i] = create(typeArgs.get(i));
        }
        return new SimpleType(
                category,
                name, 
                packageName, 
                simpleName,
                false,
                typeElement.getModifiers().contains(Modifier.FINAL),
                params);
    }

    @Nullable
    public Type create(TypeMirror type){
        List<String> key = createKey(type,true);
        if (entityTypeCache.containsKey(key)){
            return entityTypeCache.get(key);

        }else if (cache.containsKey(key)){
            return cache.get(key);

        }else{
            cache.put(key, null);
            Type typeModel = handle(type);
            if (typeModel != null && typeModel.getCategory() == TypeCategory.ENTITY){
                EntityType entityType = createEntityType(type);
                cache.put(key, entityType);
                return entityType;
            }else{
                cache.put(key, typeModel);
                return typeModel;
            }
        }
    }

    private Type createClassType(DeclaredType t, TypeElement typeElement) {
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
                && isImplemented(typeElement, comparableType)
                && isSubType(typeElement, numberType)){
            typeCategory = TypeCategory.NUMERIC;
            
        }else if (!typeCategory.isSubCategoryOf(TypeCategory.COMPARABLE)
                && isImplemented(typeElement, comparableType)){
            typeCategory = TypeCategory.COMPARABLE;
        }
        return create(typeElement, typeCategory, t.getTypeArguments());
    }

    private Type createCollectionType(String simpleName,
            Iterator<? extends TypeMirror> i) {
        if (!i.hasNext()){
            throw new TypeArgumentsException(simpleName);
        }
        return new SimpleType(Types.COLLECTION, create(i.next()));
    }

    @Nullable
    public EntityType createEntityType(TypeMirror type){
        List<String> key = createKey(type, true);
        if (entityTypeCache.containsKey(key)){
            return entityTypeCache.get(key);

        }else{
            entityTypeCache.put(key, null);
            Type value = handle(type);
            if (value != null){
                EntityType entityModel = new EntityType(configuration.getNamePrefix(), value);
                entityTypeCache.put(key, entityModel);

                if (key.size() > 1 && key.get(0).equals(entityModel.getFullName()) && doubleIndexEntities){
                    List<String> newKey = new ArrayList<String>();
                    newKey.add(entityModel.getFullName());
                    for (int i = 0; i < entityModel.getParameters().size(); i++){
                        newKey.add("?");
                    }
                    if (!entityTypeCache.containsKey(newKey)){
                        entityTypeCache.put(newKey, entityModel);
                    }
                }

                for (Type superType : getSupertypes(type, value)){
                    entityModel.addSupertype(new Supertype(superType));
                }
                return entityModel;
            }else{
                return null;
            }
        }
    }

    private Type createEnumType(DeclaredType t, TypeElement typeElement) {
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, t.getTypeArguments());
            }
        }

        // fallback
        return create(typeElement, TypeCategory.ENUM, t.getTypeArguments());
    }

    private Type createInterfaceType(DeclaredType t, TypeElement typeElement) {
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, t.getTypeArguments());
            }
        }

        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        Iterator<? extends TypeMirror> i = t.getTypeArguments().iterator();
        Class<?> cl = safeClassForName(name);

        if (cl == null) { // class not available
            return create(typeElement, TypeCategory.get(name), t.getTypeArguments());

        }else if (Map.class.isAssignableFrom(cl)){
            return createMapType(simpleName, i);

        } else if (List.class.isAssignableFrom(cl)) {
            return createListType(simpleName, i);

        } else if (Set.class.isAssignableFrom(cl)) {
            return createSetType(simpleName, i);

        } else if (Collection.class.isAssignableFrom(cl)) {
            return createCollectionType(simpleName, i);

        }else{
            return create(typeElement, TypeCategory.get(name), t.getTypeArguments());
        }
    }

    private List<String> createKey(TypeMirror type, boolean deep){
        List<String> key = new ArrayList<String>();
        String name = type.toString();
        if (name.contains("<")){
            name = name.substring(0, name.indexOf('<'));
        }
        key.add(name);

        if (type.getKind() == TypeKind.TYPEVAR){
            appendToKey(key, (TypeVariable) type);
        }else if (type.getKind() == TypeKind.WILDCARD){
            appendToKey(key, (WildcardType)type);
        }else if (type.getKind() == TypeKind.DECLARED){
            appendToKey(key, (DeclaredType)type, deep);
        }
        return key;
    }

    private Type createListType(String simpleName,
            Iterator<? extends TypeMirror> i) {
        if (!i.hasNext()){
            throw new TypeArgumentsException(simpleName);
        }
        return new SimpleType(Types.LIST, create(i.next()));
    }

    private Type createMapType(String simpleName,
            Iterator<? extends TypeMirror> i) {
        if (!i.hasNext()){
            throw new TypeArgumentsException(simpleName);
        }
        return new SimpleType(Types.MAP, create(i.next()), create(i.next()));
    }

    private Type createSetType(String simpleName,
            Iterator<? extends TypeMirror> i) {
        if (!i.hasNext()){
            throw new TypeArgumentsException(simpleName);
        }
        return new SimpleType(Types.SET, create(i.next()));
    }

    private Set<Type> getSupertypes(TypeMirror type, Type value) {
        boolean doubleIndex = doubleIndexEntities;
        doubleIndexEntities = false;
        Set<Type> superTypes = Collections.emptySet();
        type = normalize(type);
        if (type.getKind() == TypeKind.DECLARED){
            DeclaredType declaredType = (DeclaredType)type;
            TypeElement e = (TypeElement)declaredType.asElement();
            // class
            if (e.getKind() == ElementKind.CLASS){
                if (e.getSuperclass().getKind() != TypeKind.NONE){
                    TypeMirror supertype = normalize(e.getSuperclass());
                    Type superClass = create(supertype);
                    if (!superClass.getFullName().startsWith("java")){
                        superTypes = Collections.singleton(create(supertype));
                    }
                }
            // interface
            }else{
                superTypes = new HashSet<Type>(e.getInterfaces().size());
                for (TypeMirror mirror : e.getInterfaces()){
                    Type iface = create(mirror);
                    if (!iface.getFullName().startsWith("java")){
                        superTypes.add(iface);
                    }
                }
            }

        }else{
            throw new IllegalArgumentException("Unsupported type kind " + type.getKind());
        }
        doubleIndexEntities = doubleIndex;
        return superTypes;
    }

    @Nullable
    private Type handle(TypeMirror type) {
        if (type instanceof DeclaredType){
            return handleDeclaredType((DeclaredType)type);
        }else if (type instanceof TypeVariable){
            return handleTypeVariable((TypeVariable)type);
        }else if (type instanceof WildcardType){
            return handleWildcard((WildcardType)type);
        }else if (type instanceof ArrayType){
            ArrayType t = (ArrayType)type;
            return create(t.getComponentType()).asArrayType();
        }else if (type instanceof PrimitiveType){
            return handlePrimitiveType((PrimitiveType)type);
        }else if (type instanceof NoType){
            return defaultValue;
        }else{
            return null;
        }
    }

    private Type handleDeclaredType(DeclaredType t) {
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
    }

    private Type handlePrimitiveType(PrimitiveType t) {
        switch (t.getKind()) {
        case BOOLEAN: return Types.BOOLEAN;
        case BYTE: return Types.BYTE;
        case CHAR: return Types.CHARACTER;
        case DOUBLE: return Types.DOUBLE;
        case FLOAT: return Types.FLOAT;
        case INT: return Types.INTEGER;
        case LONG: return Types.LONG;
        case SHORT: return Types.SHORT;
        }
        throw new IllegalArgumentException("Unsupported type " + t.getKind());
    }

    @Nullable
    private Type handleTypeVariable(TypeVariable t) {
        String varName = t.toString();
        if (t.getUpperBound() != null){
            return new TypeExtends(varName, handle(t.getUpperBound()));
        }else if (t.getLowerBound() != null && !(t.getLowerBound() instanceof NullType)){
            return new TypeSuper(varName, handle(t.getLowerBound()));
        }else{
            return null;
        }
    }

    @Nullable
    private Type handleWildcard(WildcardType t) {
        if (t.getExtendsBound() != null){
            return new TypeExtends(handle(t.getExtendsBound()));
        }else if (t.getSuperBound() != null){
            return new TypeSuper(handle(t.getSuperBound()));
        }else{
            return null;
        }
    }

    // TODO : simplify this
    private boolean isImplemented(TypeElement type, TypeElement iface) {
        for (TypeMirror t : type.getInterfaces()){
            String name = t.toString();
            if (name.contains("<")){
                name = name.substring(0, name.indexOf('<'));
            }
            // interface is directly implemented
            if (name.equals(iface.getQualifiedName().toString())){
                return true;
            }
        }
        if (type.getSuperclass() != null){
            TypeElement superType = (TypeElement) env.getTypeUtils().asElement(type.getSuperclass());
            if (superType != null){
                return isImplemented(superType, iface);
            }
            superType = env.getElementUtils().getTypeElement(type.getSuperclass().toString());
            if (superType != null){
                return isImplemented(superType, iface);
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean isSubType(TypeElement type1, TypeElement type2) {
        return env.getTypeUtils().isSubtype(type1.asType(), type2.asType());
    }

    private TypeMirror normalize(TypeMirror type) {
        if (type.getKind() == TypeKind.TYPEVAR){
            TypeVariable typeVar = (TypeVariable)type;
            if (typeVar.getUpperBound() != null){
                return typeVar.getUpperBound();
            }
        }else if (type.getKind() == TypeKind.WILDCARD){
            WildcardType wildcard = (WildcardType)type;
            if (wildcard.getExtendsBound() != null){
                return wildcard.getExtendsBound();
            }
        }
        return type;
    }

}
