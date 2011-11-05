package com.mysema.query.apt;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.AbstractTypeVisitor6;

/**
 * @author tiwe
 *
 */
public class TypeExtractor extends AbstractTypeVisitor6<TypeElement, Void> {

    private final boolean skipEnum;
    
    public TypeExtractor(boolean skipEnum) {
        this.skipEnum = skipEnum;
    }
    
    @Override
    public TypeElement visitPrimitive(PrimitiveType t, Void p) {
        return null;
    }

    @Override
    public TypeElement visitNull(NullType t, Void p) {
        return null;
    }

    @Override
    public TypeElement visitArray(ArrayType t, Void p) {
        return visit(t.getComponentType());
    }

    @Override
    public TypeElement visitDeclared(DeclaredType t, Void p) {
        if (t.asElement() instanceof TypeElement) {
            TypeElement typeElement = (TypeElement)t.asElement();
            switch (typeElement.getKind()) {
                case ENUM:      return skipEnum ? null : typeElement;
                case CLASS:     return typeElement;
                case INTERFACE: return visit(t.getTypeArguments().get(t.getTypeArguments().size()-1));
                default: throw new IllegalArgumentException("Illegal type " + typeElement);
            }
        } else {
            return null;
        }
    }

    @Override
    public TypeElement visitError(ErrorType t, Void p) {
        return visitDeclared(t, p);
    }

    @Override
    public TypeElement visitTypeVariable(TypeVariable t, Void p) {
        if (t.getUpperBound() != null) {
            return visit(t.getUpperBound());
        } else {
            return visit(t.getLowerBound());
        }
    }

    @Override
    public TypeElement visitWildcard(WildcardType t, Void p) {
        return visit(t.getExtendsBound());
    }

    @Override
    public TypeElement visitExecutable(ExecutableType t, Void p) {
        return null;
    }

    @Override
    public TypeElement visitNoType(NoType t, Void p) {
        return null;
    }
       
    
}
