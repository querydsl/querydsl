/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;

import com.mysema.query.codegen.ClassTypeModel;
import com.mysema.query.codegen.SimpleTypeModel;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeModel;
import com.mysema.query.codegen.TypeModelFactory;
import com.mysema.query.util.TypeUtil;

/**
 * @author tiwe
 *
 */
public class APTModelFactory implements TypeVisitor<TypeModel,Elements> {

    private final ProcessingEnvironment env;
    
    private final TypeModelFactory factory;
    
    private final TypeModel defaultValue;
    
    private final Map<String,TypeModel> cache = new HashMap<String,TypeModel>();
    
    private final List<Class<? extends Annotation>> entityAnnotations;
    
    private final TypeElement numberType, comparableType;
    
    public APTModelFactory(ProcessingEnvironment env, TypeModelFactory factory, List<Class<? extends Annotation>> annotations){
        this.env = env;
        this.factory = factory;
        this.defaultValue = factory.create(Object.class);       
        this.entityAnnotations = annotations;
        this.numberType = env.getElementUtils().getTypeElement(Number.class.getName());
        this.comparableType = env.getElementUtils().getTypeElement(Comparable.class.getName());        
    }
    
    public TypeModel create(TypeMirror type, Elements el){
        String key = type.toString();
        if (cache.containsKey(key)){
            return cache.get(key);
        }else{
            TypeModel value = type.accept(this, el);
            cache.put(key, value);
            return value;
        }
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
            case CLASS: return createClassType(typeElement, p);
            case INTERFACE: return createInterfaceType(t, typeElement, p);
            case ENUM: return create(typeElement, TypeCategory.SIMPLE, p);
            }            
        }else{
            throw new IllegalArgumentException("Unsupported element type " + t.asElement());
        }
        return null;
    }

    private TypeModel createInterfaceType(DeclaredType t, TypeElement typeElement, Elements p) {
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, p);
            }
        }       
                
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        Iterator<? extends TypeMirror> i = t.getTypeArguments().iterator();
        Class<?> cl = TypeUtil.safeForName(name);
        if (cl == null) { // class not available
            return create(typeElement, TypeCategory.get(name), p);
            
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
            
        } else if (Collection.class.isAssignableFrom(cl)) {
            if (!i.hasNext()){
                throw new TypeArgumentsException(simpleName);
            }                    
            return factory.createCollectionType(create(i.next(), p));
        
        }else{
            return create(typeElement, TypeCategory.get(name), p);
        }
    }

    private TypeModel createClassType(TypeElement typeElement, Elements p) {   
        // entity type
        for (Class<? extends Annotation> entityAnn : entityAnnotations){
            if (typeElement.getAnnotation(entityAnn) != null){
                return create(typeElement, TypeCategory.ENTITY, p);
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
        return create(typeElement, typeCategory, p);
    }

    private boolean isSubType(TypeElement type1, TypeElement type2) {
        return env.getTypeUtils().isSubtype(type1.asType(), type2.asType());
    }

    private boolean isAssignable(TypeElement type1, TypeElement type2) {
        TypeMirror t1 = type1.asType();
        TypeMirror t2 = env.getTypeUtils().erasure(type2.asType());
        return env.getTypeUtils().isAssignable(t1, t2);
    }

    
    private TypeModel create(TypeElement typeElement, TypeCategory category, Elements p) {
        String name = typeElement.getQualifiedName().toString();
        String simpleName = typeElement.getSimpleName().toString();
        String packageName = p.getPackageOf(typeElement).getQualifiedName().toString();
        return new SimpleTypeModel(category, name, packageName, simpleName, null, null);
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
            return new ClassTypeModel(TypeCategory.BOOLEAN, Boolean.class, boolean.class);
        case BYTE:
            return new ClassTypeModel(TypeCategory.NUMERIC, Byte.class, byte.class);
        case CHAR:
            return new ClassTypeModel(TypeCategory.COMPARABLE, Character.class, char.class);
        case DOUBLE:
            return new ClassTypeModel(TypeCategory.NUMERIC, Double.class, double.class);
        case FLOAT:
            return new ClassTypeModel(TypeCategory.NUMERIC, Float.class, float.class);
        case INT:
            return new ClassTypeModel(TypeCategory.NUMERIC, Integer.class, int.class);
        case LONG:
            return new ClassTypeModel(TypeCategory.NUMERIC, Long.class, long.class);
        case SHORT:
            return new ClassTypeModel(TypeCategory.NUMERIC, Short.class, short.class);
        }
        throw new IllegalArgumentException("Unsupported type " + t);
    }

    @Override
    public TypeModel visitTypeVariable(TypeVariable t, Elements p) {
        if (t.getUpperBound() != null){
            return t.getUpperBound().accept(this, p);
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
            return t.getExtendsBound().accept(this, p);
        }else{
            return null;
        }
    }
    
}
