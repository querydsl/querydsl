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
package com.mysema.codegen.model;

import java.util.Collections;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * @author tiwe
 * 
 */
public class TypeExtends extends TypeAdapter {

    private final String varName;

    public TypeExtends(String varName, Type type) {
        super(type);
        this.varName = varName;
    }

    public TypeExtends(Type type) {
        super(type);
        varName = null;
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return getGenericName(asArgType, Collections.<String> emptySet(),
                Collections.<String> emptySet());
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        if (!asArgType) {
            if (Types.OBJECT.equals(type)) {
                return "?";
            } else {
                String genericName = super.getGenericName(true, packages, classes);
                return Strings.isNullOrEmpty(genericName) ? "?" : "? extends " + genericName;
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
        } else if (o instanceof TypeExtends) {
            return Objects.equal(((TypeExtends) o).varName, varName)
                && ((TypeExtends) o).type.equals(type);
        } else {
            return false;
        }
    }

}
