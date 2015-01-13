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
package com.querydsl.codegen;

import java.util.HashMap;
import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.TemplateExpression;

/**
 * TypeMappings defines mappings from Java types to {@link Expression}, {@link Path} and 
 * {@link TemplateExpression} types
 *
 * @author tiwe
 *
 */
public abstract class TypeMappings {

    private final Map<String, Type> queryTypes = new HashMap<String, Type>();

    private final Map<TypeCategory, Type> exprTypes
            = new EnumMap<TypeCategory, Type>(TypeCategory.class);

    private final Map<TypeCategory, Type> pathTypes
            = new EnumMap<TypeCategory, Type>(TypeCategory.class);

    private final Map<TypeCategory, Type> templateTypes
            = new EnumMap<TypeCategory, Type>(TypeCategory.class);
    
    public Type getTemplateType(Type type, EntityType model, boolean raw) {
        return getTemplateType(type, model, raw, false, false);
    }

    public Type getTemplateType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend) {
        return getQueryType(templateTypes, type, model, raw, rawParameters, extend);
    }

    public Type getExprType(Type type, EntityType model, boolean raw) {
        return getExprType(type, model, raw, false, false);
    }

    public Type getExprType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend) {
        if (queryTypes.containsKey(type.getFullName())) {
            return queryTypes.get(type.getFullName());
        } else {
            return getQueryType(exprTypes, type, model, raw, rawParameters, extend);
        }
    }

    public Type getPathType(Type type, EntityType model, boolean raw) {
        return getPathType(type, model, raw, false, false);
    }

    public Type getPathType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend) {
        if (queryTypes.containsKey(type.getFullName())) {
            return queryTypes.get(type.getFullName());
        } else {
            return getQueryType(pathTypes, type, model, raw, rawParameters, extend);
        }
    }

    private Type getQueryType(Map<TypeCategory, Type> types, Type type, EntityType model, boolean raw, 
            boolean rawParameters, boolean extend) {
        Type exprType = types.get(type.getCategory());
        return getQueryType(type, model, exprType, raw, rawParameters, extend);
    }

    public Type getQueryType(Type type, EntityType model, Type exprType, boolean raw, 
            boolean rawParameters, boolean extend) {
        TypeCategory category = type.getCategory();
        if (raw && category != TypeCategory.ENTITY && category != TypeCategory.CUSTOM) {
            return exprType;

        } else if (category == TypeCategory.STRING || category == TypeCategory.BOOLEAN) {
            return exprType;

        } else {
            if (rawParameters) {
                type = new SimpleType(type);
            }
            if (!type.isFinal() && extend) {
                type = new TypeExtends(type);
            }
            return new SimpleType(exprType, type);

        }
    }
    
    @SuppressWarnings("rawtypes")
    public void register(TypeCategory category,
            @Nullable Class<? extends Expression> expr,
            @Nullable Class<? extends Path> path,
            @Nullable Class<? extends TemplateExpression> template) {
        if (expr != null) {
            exprTypes.put(category, new ClassType(expr));
        }
        if (path != null) {
            pathTypes.put(category, new ClassType(path));
        }
        if (template != null) {
            templateTypes.put(category, new ClassType(template));
        }
    }

    public void register(Type type, Type queryType) {
        queryTypes.put(type.getFullName(), queryType);
    }

    public boolean isRegistered(Type type) {
        return queryTypes.containsKey(type.getFullName());
    }
}
