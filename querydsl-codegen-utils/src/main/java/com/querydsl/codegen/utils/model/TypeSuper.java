/*
 * Copyright 2010, Mysema Ltd
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
package com.querydsl.codegen.utils.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * TypeSuper is a Type for type variables and wildcard types
 * 
 * @author tiwe
 * 
 */
public class TypeSuper extends TypeAdapter {

    private final Type superType;

    private final String varName;

    public TypeSuper(String varName, Type type) {
        super(Types.OBJECT);
        this.superType = type;
        this.varName = varName;
    }

    public TypeSuper(Type type) {
        super(Types.OBJECT);
        this.superType = type;
        this.varName = null;
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return getGenericName(asArgType, Collections.<String> emptySet(),
                Collections.<String> emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        if (!asArgType) {
            if (superType instanceof TypeExtends) {
                return "?";
            } else {
                return "? super " + superType.getGenericName(true, packages, classes);    
            }            
            
        } else {
            return super.getGenericName(asArgType, packages, classes);
        }
    }

    public String getVarName() {
        return varName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof TypeSuper) {
            return Objects.equals(((TypeSuper) o).varName, varName)
                    && ((TypeSuper) o).superType.equals(superType);
        } else {
            return false;
        }
    }
}
