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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;

import com.mysema.query.codegen.EntityModel;
import com.mysema.query.codegen.SimpleClassTypeModel;
import com.mysema.query.codegen.SimpleTypeModel;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeExtendsModel;
import com.mysema.query.codegen.TypeModel;
import com.mysema.query.codegen.TypeModelFactory;
import com.mysema.query.codegen.TypeSuperModel;
import com.mysema.query.util.TypeUtil;

/**
 * APTTypeModelFactory is a factory for APT inspection based TypeModel creation
 * 
 * @author tiwe
 *
 */
// TODO : simplify
public class APTTypeModelFactory implements TypeVisitor<TypeModel,Elements> {
    
    private final Configuration configuration;

    private final ProcessingEnvironment env;
    
    private final TypeModelFactory factory;
    
    private final TypeModel defaultValue;
    
    private final Map<List<String>,EntityModel> entityTypeCache = new HashMap<List<String>,EntityModel>();
    
    private final Map<List<String>,TypeModel> cache = new HashMap<List<String>,TypeModel>();
    
    private final List<Class<? extends Annotation>> entityAnnotations;
    
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
    
    /**
     * Create an EntityModel for the given TypeMirror
     * 
     * @param type
     * @param el
     * @return
     */
    public EntityModel createEntityModel(TypeMirror type, Elements el){
        List<String> key = getKey(type, true);
        if (entityTypeCache.containsKey(key)){
            return entityTypeCache.get(key);
        
        }else{
            entityTypeCache.put(key, null);
            TypeModel value = type.accept(this, el);
            if (value != null){                
                EntityModel entityModel = asEntityModel(type, el, value);
                entityTypeCache.put(key, entityModel);
                return entityModel;
            }else{
                return null;
            }
        }
    }
    
    /**
     * Create a TypeModel for the given TypeMirror
     * 
     * @param type
     * @param el
     * @return
     */
    public TypeModel create(TypeMirror type, Elements el){
        List<String> key = getKey(type, true);  
        if (entityTypeCache.containsKey(key)){
            return entityTypeCache.get(key);
            
        }else if (cache.containsKey(key)){
            return cache.get(key);
            
        }else{
            cache.put(key, null);
            TypeModel value = type.accept(this, el);
            if (value != null && value.getCategory() == TypeCategory.ENTITY){                
                value = asEntityModel(type, el, value);
                entityTypeCache.put(key, (EntityModel)value);
            }else{
                cache.put(key, value);    
            }            
            return value;
        }        
    }

    private EntityModel asEntityModel(TypeMirror type, Elements el, TypeModel value) {                 
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
                superTypes = Collections.singleton(create(e.getSuperclass(), el).getFullName());
            }else{
                superTypes = new ArrayList<String>(e.getInterfaces().size());
                for (TypeMirror mirror : e.getInterfaces()){
                    TypeModel iface = create(mirror, el);
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

    @Override
    public TypeModel visit(TypeMirror t) {
        throw new UnsupportedTypeException(t);
    }

    @Override
    public TypeModel visit(TypeMirror t, Elements p) {
        throw new UnsupportedTypeException(t);
    }

    @Override
    public TypeModel visitArray(ArrayType t, Elements p) {
        return factory.createArrayType(create(t.getComponentType(), p));
    }

    @Override
    public TypeModel visitDeclared(DeclaredType t, Elements p) {
        if (t.asElement() != null && t.asElement() instanceof TypeElement){
            TypeElement typeElement = (TypeElement)t.asElement();
            switch(typeElement.getKind()){
            case ENUM:      return createEnumType(t, typeElement, p);
            case CLASS:     return createClassType(t, typeElement, p);
            case INTERFACE: return createInterfaceType(t, typeElement, p);
            default: throw new IllegalArgumentException("Illegal type " + typeElement);
            }            
        }else{
            throw new IllegalArgumentException("Unsupported element type " + t.asElement());
        }
    }

    private TypeModel createInterfaceType(DeclaredType t, TypeElement typeElement, Elements p) {
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, p, t.getTypeArguments());
            }
        }       
                
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        Iterator<? extends TypeMirror> i = t.getTypeArguments().iterator();
        Class<?> cl = TypeUtil.safeForName(name);
        if (cl == null) { // class not available
            return create(typeElement, TypeCategory.get(name), p, t.getTypeArguments());
            
        }else if (Map.class.isAssignableFrom(cl)){
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createMapType(create(i.next(), p), create(i.next(), p));

        } else if (List.class.isAssignableFrom(cl)) {
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createListType(create(i.next(), p));
            
        } else if (Set.class.isAssignableFrom(cl)) {
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createSetType(create(i.next(), p));
            
            
        } else if (Collection.class.isAssignableFrom(cl)) {
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createCollectionType(create(i.next(), p));
        
        }else{
            return create(typeElement, TypeCategory.get(name), p, t.getTypeArguments());
        }
    }
    
    private TypeModel createEnumType(DeclaredType t, TypeElement typeElement, Elements p) {
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, p, t.getTypeArguments());
            }
        }  
        
        // fallback
        return create(typeElement, TypeCategory.SIMPLE, p, t.getTypeArguments());
    }

    private TypeModel createClassType(DeclaredType t, TypeElement typeElement, Elements p) {   
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, p, t.getTypeArguments());
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
        return create(typeElement, typeCategory, p, t.getTypeArguments());
    }

    private boolean isSubType(TypeElement type1, TypeElement type2) {
        return env.getTypeUtils().isSubtype(type1.asType(), type2.asType());
    }

    private boolean isAssignable(TypeElement type1, TypeElement type2) {
        TypeMirror t1 = type1.asType();
        TypeMirror t2 = env.getTypeUtils().erasure(type2.asType());
        return env.getTypeUtils().isAssignable(t1, t2);
    }

    
    private TypeModel create(TypeElement typeElement, TypeCategory category, 
            Elements p, List<? extends TypeMirror> typeArgs) {
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        String packageName = p.getPackageOf(typeElement).getQualifiedName().toString();
        TypeModel[] params = new TypeModel[typeArgs.size()];
        for (int i = 0; i < params.length; i++){
            params[i] = create(typeArgs.get(i), p);
        }        
        return new SimpleTypeModel(category, 
            name, packageName, simpleName, 
            typeElement.getModifiers().contains(Modifier.FINAL), 
            params);
    }

    @Override
    public TypeModel visitError(ErrorType t, Elements p) {
        throw new UnsupportedTypeException(t);
    }

    @Override
    public TypeModel visitExecutable(ExecutableType t, Elements p) {
        throw new UnsupportedTypeException(t);
    }

    @Override
    public TypeModel visitNoType(NoType t, Elements p) {
        return defaultValue;
    }

    @Override
    public TypeModel visitNull(NullType t, Elements p) {
        throw new UnsupportedTypeException(t);
    }

    @Override
    public TypeModel visitPrimitive(PrimitiveType t, Elements p) {
        switch (t.getKind()) {
        case BOOLEAN:
            return new SimpleClassTypeModel(TypeCategory.BOOLEAN, Boolean.class, boolean.class);
        case BYTE:
            return new SimpleClassTypeModel(TypeCategory.NUMERIC, Byte.class, byte.class);
        case CHAR:
            return new SimpleClassTypeModel(TypeCategory.COMPARABLE, Character.class, char.class);
        case DOUBLE:
            return new SimpleClassTypeModel(TypeCategory.NUMERIC, Double.class, double.class);
        case FLOAT:
            return new SimpleClassTypeModel(TypeCategory.NUMERIC, Float.class, float.class);
        case INT:
            return new SimpleClassTypeModel(TypeCategory.NUMERIC, Integer.class, int.class);
        case LONG:
            return new SimpleClassTypeModel(TypeCategory.NUMERIC, Long.class, long.class);
        case SHORT:
            return new SimpleClassTypeModel(TypeCategory.NUMERIC, Short.class, short.class);
        }
        throw new IllegalArgumentException("Unsupported type " + t);
    }

    @Override
    public TypeModel visitTypeVariable(TypeVariable t, Elements p) {
        // TODO : take variable name into account
        if (t.getUpperBound() != null){
            return new TypeExtendsModel(t.getUpperBound().accept(this, p));
        }else if (t.getLowerBound() != null && !(t.getLowerBound() instanceof NullType)){
            return new TypeSuperModel(t.getLowerBound().accept(this, p));
        }else{
            return null;
        }        
    }

    @Override
    public TypeModel visitUnknown(TypeMirror t, Elements p) {
        throw new UnsupportedTypeException(t);
    }

    @Override
    public TypeModel visitWildcard(WildcardType t, Elements p) {        
        if (t.getExtendsBound() != null){
            return new TypeExtendsModel(t.getExtendsBound().accept(this, p));
        }else if (t.getSuperBound() != null){
            return new TypeSuperModel(t.getSuperBound().accept(this, p));
        }else{            
            return null;
        }
        
    }
    
}
