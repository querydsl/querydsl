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
package com.querydsl.apt;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;

/**
 * TypeExtractor is a visitor implementation which a concrete type given a general {@link TypeElement}
 * 
 * @author tiwe
 *
 */
public class TypeExtractor extends SimpleTypeVisitorAdapter<TypeElement, Void> {

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
                case INTERFACE: return visitInterface(t);
                default: throw new IllegalArgumentException("Illegal type " + typeElement);
            }
        } else {
            return null;
        }
    }

    private TypeElement visitInterface(DeclaredType t) {
        if (t.getTypeArguments().isEmpty()) {
            return (TypeElement)t.asElement();
        } else {
            int count = t.getTypeArguments().size();
            if (t.asElement().toString().startsWith("java.util")) {
                return t.getTypeArguments().get(count - 1).accept(this, null);
            } else {
                return (TypeElement)t.asElement();
            }
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
        } else if (t.getLowerBound() != null) {
            return visit(t.getLowerBound());
        } else {
            return null;
        }
    }

    @Override
    public TypeElement visitWildcard(WildcardType t, Void p) {
        if (t.getExtendsBound() != null) {
            return visit(t.getExtendsBound());    
        } else if (t.getSuperBound() != null) {    
            return visit(t.getSuperBound());
        } else {
            return null;
        }
        
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
