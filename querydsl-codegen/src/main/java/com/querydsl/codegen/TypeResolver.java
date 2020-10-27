/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.codegen;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.TypeSuper;

import java.util.Objects;

/**
 * {@code TypeResolver} provides type resolving functionality for resolving generic type variables to
 * concrete types
 *
 * @author tiwe
 *
 */
final class TypeResolver {

    /**
     * Resolve type declared in declaringType for context
     *
     * @param type type to be resolved
     * @param declaringType declaration context of type
     * @param context target context of type
     * @return resolved type
     */
    public static Type resolve(Type type, Type declaringType, EntityType context) {
        Type resolved = unwrap(type);

        String varName = getVarName(resolved);
        if (varName != null) {
            resolved = resolveVar(resolved, varName, declaringType, context);
        } else if (!resolved.getParameters().isEmpty()) {
            resolved = resolveWithParameters(resolved, declaringType, context);
        }

        // rewrap entity type
        if (type instanceof EntityType) {
            if (!unwrap(type).equals(resolved)) {
                resolved = new EntityType(resolved, ((EntityType) type).getSuperTypes());
            } else {
                // reset to original type
                resolved = type;
            }
        }

        return resolved;
    }

    private static Type resolveVar(Type resolved, String varName, Type declaringType, EntityType context) {
        // get parameter index of var in declaring type
        int index = -1;
        for (int i = 0; i < declaringType.getParameters().size(); i++) {
            Type param = unwrap(declaringType.getParameters().get(i));
            if (Objects.equals(getVarName(param), varName)) {
                index = i;
            }
        }

        if (index == -1) {
            throw new IllegalStateException("Did not find type " + varName
                    + " in " + declaringType + " for " + context);
        }

        Supertype type = context.getSuperType();
        while (!type.getEntityType().equals(declaringType)) {
            type = type.getEntityType().getSuperType();
        }
        if (!type.getType().getParameters().isEmpty()) {
            return type.getType().getParameters().get(index);
        } else {
            // raw type
            return resolved;
        }
    }

    private static Type resolveWithParameters(Type type, Type declaringType, EntityType context) {
        Type[] params = new Type[type.getParameters().size()];
        boolean transformed = false;
        for (int i = 0; i < type.getParameters().size(); i++) {
            Type param = type.getParameters().get(i);
            if (param != null && !param.getFullName().equals(type.getFullName())) {
                params[i] = resolve(param, declaringType, context);
                if (!params[i].equals(param)) {
                    transformed = true;
                }
            }
        }
        if (transformed) {
            return new SimpleType(type, params);
        } else {
            return type;
        }
    }

    private static String getVarName(Type type) {
        if (type instanceof TypeExtends) {
            return ((TypeExtends) type).getVarName();
        } else if (type instanceof TypeSuper) {
            return ((TypeSuper) type).getVarName();
        } else {
            return null;
        }
    }

    private static Type unwrap(Type type) {
        if (type instanceof EntityType) {
            return ((EntityType) type).getInnerType();
        } else {
            return type;
        }
    }

    private TypeResolver() { }
}
