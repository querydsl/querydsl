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
import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.Supertype;
import com.mysema.query.codegen.TypeMappings;

/**
 * ExtendedTypeFactory is a factory for APT inspection based Type creation
 *
 * @author tiwe
 *
 */
public final class ExtendedTypeFactory {

    @Nullable
    private static Class<?> safeClassForName(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private final Map<List<String>, Type> typeCache = new HashMap<List<String>, Type>();

    private final Type defaultType;

    private final Set<Class<? extends Annotation>> entityAnnotations;

    private final Map<List<String>, EntityType> entityTypeCache = new HashMap<List<String>, EntityType>();

    private final ProcessingEnvironment env;

    private final TypeElement numberType, comparableType;

    private boolean doubleIndexEntities = true;

    private final TypeMappings typeMappings;

    private final QueryTypeFactory queryTypeFactory;

    public ExtendedTypeFactory(
            ProcessingEnvironment env,
            Configuration configuration,
            Set<Class<? extends Annotation>> annotations,
            TypeMappings typeMappings,
            QueryTypeFactory queryTypeFactory){
        this.env = env;
        this.defaultType = Types.OBJECT;
        this.entityAnnotations = annotations;
        this.numberType = env.getElementUtils().getTypeElement(Number.class.getName());
        this.comparableType = env.getElementUtils().getTypeElement(Comparable.class.getName());
        this.typeMappings = typeMappings;
        this.queryTypeFactory = queryTypeFactory;
    }

    private void appendToKey(List<String> key, DeclaredType t, boolean deep) {
        for (TypeMirror arg : t.getTypeArguments()) {
            if (deep) {
                key.addAll(createKey(arg, false));
            } else {
                key.add(arg.toString());
            }
        }
    }

    private void appendToKey(List<String> key, TypeVariable t) {
        if (t.getUpperBound() != null) {
            key.addAll(createKey(t.getUpperBound(), false));
        }
        if (t.getLowerBound() != null) {
            key.addAll(createKey(t.getLowerBound(), false));
        }
    }

    private void appendToKey(List<String> key, WildcardType t) {
        if (t.getExtendsBound() != null) {
            key.addAll(createKey(t.getExtendsBound(), false));
        }
        if (t.getSuperBound() != null) {
            key.addAll(createKey(t.getSuperBound(), false));
        }
    }

    private Type createType(TypeElement typeElement, TypeCategory category, List<? extends TypeMirror> typeArgs, boolean deep) {
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        String packageName = env.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        Type[] params = new Type[typeArgs.size()];
        for (int i = 0; i < params.length; i++) {
            params[i] = getType(typeArgs.get(i), deep);
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
    public Type getType(TypeMirror typeMirror, boolean deep){
        if (typeMirror.getKind().isPrimitive()) {
            typeMirror = normalizePrimitiveType(typeMirror);
        }
        List<String> key = createKey(typeMirror,true);
        if (entityTypeCache.containsKey(key)) {
            return entityTypeCache.get(key);
        } else if (typeCache.containsKey(key)) {
            return typeCache.get(key);
        } else {
            return createType(typeMirror, key, deep);
        }
    }

    @Nullable
    private Type createType(TypeMirror typeMirror, List<String> key, boolean deep) {
        typeCache.put(key, null);
        Type type = handle(typeMirror, deep);
        if (type != null && (type.getCategory() == TypeCategory.ENTITY || type.getCategory() == TypeCategory.CUSTOM)) {
            EntityType entityType = getEntityType(typeMirror, deep);
            typeCache.put(key, entityType);
            return entityType;
        } else {
            typeCache.put(key, type);
            return type;
        }
    }

    private Type createClassType(DeclaredType declaredType, TypeElement typeElement, boolean deep) {
        // other
        String name = typeElement.getQualifiedName().toString();
        TypeCategory typeCategory = TypeCategory.get(name);
        
        if (typeCategory != TypeCategory.NUMERIC
                && isImplemented(typeElement, comparableType)
                && isSubType(typeElement, numberType)) {
            typeCategory = TypeCategory.NUMERIC;
            
        } else if (!typeCategory.isSubCategoryOf(TypeCategory.COMPARABLE)
                && isImplemented(typeElement, comparableType)) {
            typeCategory = TypeCategory.COMPARABLE;
            
        } if (typeCategory == TypeCategory.SIMPLE){
            for (Class<? extends Annotation> entityAnn : entityAnnotations) {
                if (typeElement.getAnnotation(entityAnn) != null) {
                    typeCategory = TypeCategory.ENTITY;
                }
            }
        }

        // for intersection types etc
        if (name.equals("")) {
            TypeMirror type = env.getElementUtils().getTypeElement(Object.class.getName()).asType();
            if (typeCategory == TypeCategory.COMPARABLE) {
                type = env.getElementUtils().getTypeElement(Comparable.class.getName()).asType();
            }
            // find most specific type of superTypes which is a subtype of type
            List<? extends TypeMirror> superTypes = env.getTypeUtils().directSupertypes(declaredType);
            for (TypeMirror superType : superTypes) {
                if (env.getTypeUtils().isSubtype(superType, type)) {
                    type = superType;
                }
            }
            typeElement = (TypeElement)env.getTypeUtils().asElement(type);
        }
        
        Type type;
        if (typeElement.asType() instanceof DeclaredType && declaredType.getTypeArguments().isEmpty()) {
            type = createType(typeElement, typeCategory, ((DeclaredType)typeElement.asType()).getTypeArguments(), deep);
        } else {
            type = createType(typeElement, typeCategory, declaredType.getTypeArguments(), deep);
        }        

        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations) {
            if (typeElement.getAnnotation(entityAnn) != null ){
                EntityType entityType = new EntityType(type);
                typeMappings.register(entityType, queryTypeFactory.create(entityType));
                return entityType;
            }
        }
        return type;
    }

    private Type createMapType(String simpleName, Iterator<? extends TypeMirror> typeMirrors, boolean deep) {
        if (!typeMirrors.hasNext()) {
            return new SimpleType(Types.MAP, defaultType, defaultType);
        }
        Type keyType = getType(typeMirrors.next(), deep);
        if (keyType == null) {
            keyType = defaultType;
        }
        Type valueType = getType(typeMirrors.next(), deep);
        if (valueType == null) {
            valueType = defaultType;
        } else if (valueType.getParameters().isEmpty()) {
            TypeElement element = env.getElementUtils().getTypeElement(valueType.getFullName());
            if (element != null) {
                Type type = getType(element.asType(), deep);
                if (!type.getParameters().isEmpty()) {
                    valueType = new SimpleType(valueType, new Type[type.getParameters().size()]);
                }
            }
        }
        return new SimpleType(Types.MAP, keyType, valueType);
    }

    private Type createCollectionType(String simpleName, Iterator<? extends TypeMirror> typeMirrors, boolean deep) {
        return createCollectionType(Types.COLLECTION, simpleName, typeMirrors, deep);
    }

    private Type createListType(String simpleName, Iterator<? extends TypeMirror> typeMirrors, boolean deep) {
        return createCollectionType(Types.LIST, simpleName, typeMirrors, deep);
    }

    private Type createSetType(String simpleName, Iterator<? extends TypeMirror> typeMirrors, boolean deep) {
        return createCollectionType(Types.SET, simpleName, typeMirrors, deep);
    }

    private Type createCollectionType(Type baseType, String simpleName, Iterator<? extends TypeMirror> typeMirrors, boolean deep) {
        if (!typeMirrors.hasNext()){
            return new SimpleType(baseType, defaultType);
        }
        Type componentType = getType(typeMirrors.next(), deep);
        if (componentType == null) {
            componentType = defaultType;
        } else if (componentType.getParameters().isEmpty()) {
            TypeElement element = env.getElementUtils().getTypeElement(componentType.getFullName());
            if (element != null) {
                Type type = getType(element.asType(), deep);
                if (!type.getParameters().isEmpty()) {
                    componentType = new SimpleType(componentType, new Type[type.getParameters().size()]);
                }
            }
        }
        return new SimpleType(baseType, componentType);
    }

    @Nullable
    public EntityType getEntityType(TypeMirror typeMirrors, boolean deep) { 
        if (typeMirrors.getKind().isPrimitive()) {
            typeMirrors = normalizePrimitiveType(typeMirrors);
        }
        List<String> key = createKey(typeMirrors, true);
        // get from cache
        if (entityTypeCache.containsKey(key)) {
            EntityType entityType = entityTypeCache.get(key);
            if (deep && entityType.getSuperTypes().isEmpty()) {
                for (Type superType : getSupertypes(typeMirrors, entityType, deep)) {
                    entityType.addSupertype(new Supertype(superType));
                }
            }
            return entityType;

        // create
        } else {
            return createEntityType(typeMirrors, key, deep);

        }
    }

    @Nullable
    private EntityType createEntityType(TypeMirror typeMirror, List<String> key, boolean deep) {
        entityTypeCache.put(key, null);
        Type value = handle(typeMirror, deep);
        if (value != null) {
            EntityType entityType = null;
            if (value instanceof EntityType) {
                entityType = (EntityType)value;
            } else {
                entityType = new EntityType(value);
                typeMappings.register(entityType, queryTypeFactory.create(entityType));
            }
            entityTypeCache.put(key, entityType);

            if (key.size() > 1 && key.get(0).equals(entityType.getFullName()) && doubleIndexEntities) {
                List<String> newKey = new ArrayList<String>();
                newKey.add(entityType.getFullName());
                for (int i = 0; i < entityType.getParameters().size(); i++) {
                    newKey.add("?");
                }
                if (!entityTypeCache.containsKey(newKey)) {
                    entityTypeCache.put(newKey, entityType);
                }
            }

            if (deep) {
                for (Type superType : getSupertypes(typeMirror, value, deep)) {
                    entityType.addSupertype(new Supertype(superType));
                }
            }

            return entityType;
        }else{
            return null;
        }
    }

    private Type createEnumType(DeclaredType declaredType, TypeElement typeElement, boolean deep) {
        // fallback
        Type enumType = createType(typeElement, TypeCategory.ENUM, declaredType.getTypeArguments(), deep);

        for (Class<? extends Annotation> entityAnn : entityAnnotations) {
            if (typeElement.getAnnotation(entityAnn) != null) {
                EntityType entityType = new EntityType(enumType);
                typeMappings.register(entityType, queryTypeFactory.create(entityType));
                return entityType;
            }
        }
        return enumType;
    }

    private Type createInterfaceType(DeclaredType declaredType, TypeElement typeElement, boolean deep) {
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations) {
            if (typeElement.getAnnotation(entityAnn) != null) {
                return createType(typeElement, TypeCategory.ENTITY, declaredType.getTypeArguments(), deep);
            }
        }

        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        Iterator<? extends TypeMirror> i = declaredType.getTypeArguments().iterator();
        Class<?> cl = safeClassForName(name);

        if (cl == null) { // class not available
            return createType(typeElement, TypeCategory.get(name), declaredType.getTypeArguments(), deep);

        } else if (Map.class.isAssignableFrom(cl)) {
            return createMapType(simpleName, i, deep);

        } else if (List.class.isAssignableFrom(cl)) {
            return createListType(simpleName, i, deep);

        } else if (Set.class.isAssignableFrom(cl)) {
            return createSetType(simpleName, i, deep);

        } else if (Collection.class.isAssignableFrom(cl)) {
            return createCollectionType(simpleName, i, deep);

        } else {
            return createType(typeElement, TypeCategory.get(name), declaredType.getTypeArguments(), deep);
        }
    }

    private List<String> createKey(TypeMirror typeMirror, boolean deep){
        List<String> key = new ArrayList<String>();
        String name = typeMirror.toString();
        if (name.contains("<")) {
            name = name.substring(0, name.indexOf('<'));
        }
        key.add(name);

        if (typeMirror.getKind() == TypeKind.TYPEVAR) {
            appendToKey(key, (TypeVariable) typeMirror);
        } else if (typeMirror.getKind() == TypeKind.WILDCARD) {
            appendToKey(key, (WildcardType)typeMirror);
        } else if (typeMirror.getKind() == TypeKind.DECLARED) {
            appendToKey(key, (DeclaredType)typeMirror, deep);
        }
        return key;
    }


    private Set<Type> getSupertypes(TypeMirror typeMirror, Type type, boolean deep) {
        boolean doubleIndex = doubleIndexEntities;
        doubleIndexEntities = false;
        Set<Type> superTypes = Collections.emptySet();
        typeMirror = normalize(typeMirror);
        if (typeMirror.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType)typeMirror;
            TypeElement e = (TypeElement)declaredType.asElement();
            // class
            if (e.getKind() == ElementKind.CLASS) {
                if (e.getSuperclass().getKind() != TypeKind.NONE) {
                    TypeMirror supertype = normalize(e.getSuperclass());
                    Type superClass = getType(supertype, deep);
                    if (superClass == null) {
                        System.err.println("Got no type for " + supertype);
                    } else  if (!superClass.getFullName().startsWith("java")) {
                        superTypes = Collections.singleton(getType(supertype, deep));
                    }
                }
            // interface
            } else {
                superTypes = new HashSet<Type>(e.getInterfaces().size());
                for (TypeMirror mirror : e.getInterfaces()) {
                    Type iface = getType(mirror, deep);
                    if (!iface.getFullName().startsWith("java")) {
                        superTypes.add(iface);
                    }
                }
            }

        } else {
            return Collections.emptySet();
        }
        doubleIndexEntities = doubleIndex;
        return superTypes;
    }

    @Nullable
    private Type handle(TypeMirror typeMirror, boolean deep) {
        if (typeMirror instanceof DeclaredType) {
            return handleDeclaredType((DeclaredType)typeMirror, deep);
        } else if (typeMirror instanceof TypeVariable) {
            return handleTypeVariable((TypeVariable)typeMirror, deep);
        } else if (typeMirror instanceof WildcardType) {
            return handleWildcard((WildcardType)typeMirror, deep);
        } else if (typeMirror instanceof ArrayType) {
            ArrayType t = (ArrayType)typeMirror;
            return getType(t.getComponentType(), deep).asArrayType();
        } else if (typeMirror instanceof NoType) {
            return defaultType;
        } else {
            return null;
        }
    }

    private Type handleDeclaredType(DeclaredType declaredType, boolean deep) {
        if (declaredType.asElement() instanceof TypeElement) {
            TypeElement typeElement = (TypeElement)declaredType.asElement();
            switch(typeElement.getKind()){
            case ENUM:      return createEnumType(declaredType, typeElement, deep);
            case CLASS:     return createClassType(declaredType, typeElement, deep);
            case INTERFACE: return createInterfaceType(declaredType, typeElement, deep);
            default: throw new IllegalArgumentException("Illegal type " + typeElement);
            }
        } else {
            throw new IllegalArgumentException("Unsupported element type " + declaredType.asElement());
        }
    }

    private TypeMirror normalizePrimitiveType(TypeMirror typeMirror){
        switch (typeMirror.getKind()) {
        case BOOLEAN: return env.getElementUtils().getTypeElement(Boolean.class.getName()).asType();
        case BYTE: return env.getElementUtils().getTypeElement(Byte.class.getName()).asType();
        case CHAR: return env.getElementUtils().getTypeElement(Character.class.getName()).asType();
        case DOUBLE: return env.getElementUtils().getTypeElement(Double.class.getName()).asType();
        case FLOAT: return env.getElementUtils().getTypeElement(Float.class.getName()).asType();
        case INT: return env.getElementUtils().getTypeElement(Integer.class.getName()).asType();
        case LONG: return env.getElementUtils().getTypeElement(Long.class.getName()).asType();
        case SHORT: return env.getElementUtils().getTypeElement(Short.class.getName()).asType();
        }
        throw new IllegalArgumentException("Unsupported type " + typeMirror.getKind() + " for " + typeMirror);
    }

    @Nullable
    private Type handleTypeVariable(TypeVariable typeVariable, boolean deep) {
        String varName = typeVariable.toString();
        if (typeVariable.getUpperBound() != null) {
            Type type = handle(typeVariable.getUpperBound(), deep);
            return new TypeExtends(varName, type);
        } else if (typeVariable.getLowerBound() != null && !(typeVariable.getLowerBound() instanceof NullType)) {
            return new TypeSuper(varName, handle(typeVariable.getLowerBound(), deep));
        } else {
            return null;
        }
    }

    @Nullable
    private Type handleWildcard(WildcardType wildardType, boolean deep) {
        if (wildardType.getExtendsBound() != null) {
            Type type = handle(wildardType.getExtendsBound(), deep);
            return new TypeExtends(type);
        } else if (wildardType.getSuperBound() != null) {
            return new TypeSuper(handle(wildardType.getSuperBound(), deep));
        } else {
            return null;
        }
    }

    // TODO : simplify this
    private boolean isImplemented(TypeElement type, TypeElement iface) {
        for (TypeMirror t : type.getInterfaces()) {
            String name = t.toString();
            if (name.contains("<")) {
                name = name.substring(0, name.indexOf('<'));
            }
            // interface is directly implemented
            if (name.equals(iface.getQualifiedName().toString())) {
                return true;
            }
        }
        if (type.getSuperclass() != null) {
            TypeElement superType = (TypeElement) env.getTypeUtils().asElement(type.getSuperclass());
            if (superType != null) {
                return isImplemented(superType, iface);
            }
            superType = env.getElementUtils().getTypeElement(type.getSuperclass().toString());
            if (superType != null) {
                return isImplemented(superType, iface);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isSubType(TypeElement type1, TypeElement type2) {
        return env.getTypeUtils().isSubtype(type1.asType(), type2.asType());
    }

    private TypeMirror normalize(TypeMirror type) {
        if (type.getKind() == TypeKind.TYPEVAR) {
            TypeVariable typeVar = (TypeVariable)type;
            if (typeVar.getUpperBound() != null) {
                return typeVar.getUpperBound();
            }
        } else if (type.getKind() == TypeKind.WILDCARD) {
            WildcardType wildcard = (WildcardType)type;
            if (wildcard.getExtendsBound() != null) {
                return wildcard.getExtendsBound();
            }
        }
        return type;
    }
}
