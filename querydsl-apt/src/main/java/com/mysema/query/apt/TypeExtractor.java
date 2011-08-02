package com.mysema.query.apt;

import javax.annotation.Nullable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

public class TypeExtractor {

    private final boolean skipPrimitive, skipEnum;
    
    public TypeExtractor(boolean skipPrimitive, boolean skipEnum) {
        this.skipPrimitive = skipPrimitive;
        this.skipEnum = skipEnum;
    }
    
    @Nullable
    public TypeElement handle(TypeMirror typeMirror) {
        if (typeMirror instanceof DeclaredType){
            return handleDeclaredType((DeclaredType)typeMirror);
        }else if (typeMirror instanceof TypeVariable){
            return handleTypeVariable((TypeVariable)typeMirror);
        }else if (typeMirror instanceof WildcardType){
            return handleWildcard((WildcardType)typeMirror);
        }else if (typeMirror instanceof ArrayType){
            ArrayType t = (ArrayType)typeMirror;
            return handle(t.getComponentType());
        }else if (typeMirror instanceof NoType){
            return null;
        }else{
            return null;
        }
    }
    
    @Nullable
    private TypeElement handleDeclaredType(DeclaredType typeMirror) {
        if (typeMirror.asElement() instanceof TypeElement){
            TypeElement typeElement = (TypeElement)typeMirror.asElement();
            switch(typeElement.getKind()){
                case ENUM:      return handleEnumType(typeMirror, typeElement);
                case CLASS:     return handleClassType(typeMirror, typeElement);
                case INTERFACE: return handleInterfaceType(typeMirror, typeElement);
                default: throw new IllegalArgumentException("Illegal type " + typeElement);
            }
        }else{
            return null;
        }
    }

    @Nullable
    private TypeElement handleWildcard(WildcardType typeMirror) {
        return handle(typeMirror.getExtendsBound());
    }

    @Nullable
    private TypeElement handleTypeVariable(TypeVariable typeMirror) {
        if (typeMirror.getUpperBound() != null) {
            return handle(typeMirror.getUpperBound());
        }else{
            return handle(typeMirror.getLowerBound());
        }
    }

    @Nullable
    private TypeElement handleInterfaceType(DeclaredType typeMirror, TypeElement typeElement) {
        return handle(typeMirror.getTypeArguments().get(typeMirror.getTypeArguments().size()-1));
    }

    @Nullable
    private TypeElement handleClassType(DeclaredType typeMirror, TypeElement typeElement) {
        if (skipPrimitive && typeMirror.getKind().isPrimitive()){
            return null;
        }else{
            return typeElement;            
        }
    }

    @Nullable
    private TypeElement handleEnumType(DeclaredType typeMirror, TypeElement typeElement) {
        if (skipEnum) {
            return null;
        } else {
            return typeElement;
        }        
    }
    
}
