/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.EnumExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.TimeExpression;
import com.mysema.query.types.path.ArrayPath;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.TimePath;
import com.mysema.query.types.template.BooleanTemplate;
import com.mysema.query.types.template.ComparableTemplate;
import com.mysema.query.types.template.DateTemplate;
import com.mysema.query.types.template.DateTimeTemplate;
import com.mysema.query.types.template.EnumTemplate;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.query.types.template.SimpleTemplate;
import com.mysema.query.types.template.StringTemplate;
import com.mysema.query.types.template.TimeTemplate;

/**
 * TypeMappings defines mappings from Java types to {@link Expression}, {@link Path} and {@link TemplateExpression} types
 *
 * @author tiwe
 *
 */
public final class TypeMappings {

    private final Map<String, Type> queryTypes = new HashMap<String, Type>();

    private final Map<TypeCategory, Type> exprTypes = new HashMap<TypeCategory, Type>();

    private final Map<TypeCategory, Type> pathTypes = new HashMap<TypeCategory, Type>();

    private final Map<TypeCategory, Type> templateTypes = new HashMap<TypeCategory, Type>();

    public TypeMappings(){
        register(TypeCategory.STRING, StringExpression.class, StringPath.class, StringTemplate.class);
        register(TypeCategory.BOOLEAN, BooleanExpression.class, BooleanPath.class, BooleanTemplate.class);
        register(TypeCategory.COMPARABLE, ComparableExpression.class, ComparablePath.class, ComparableTemplate.class);
        register(TypeCategory.ENUM, EnumExpression.class, EnumPath.class, EnumTemplate.class);
        register(TypeCategory.DATE, DateExpression.class, DatePath.class, DateTemplate.class);
        register(TypeCategory.DATETIME, DateTimeExpression.class, DateTimePath.class, DateTimeTemplate.class);
        register(TypeCategory.TIME, TimeExpression.class, TimePath.class, TimeTemplate.class);
        register(TypeCategory.NUMERIC, NumberExpression.class, NumberPath.class, NumberTemplate.class);
        register(TypeCategory.SIMPLE, Expression.class, SimplePath.class, SimpleTemplate.class);

        register(TypeCategory.ARRAY, Expression.class, ArrayPath.class, SimpleTemplate.class);
        register(TypeCategory.COLLECTION, Expression.class, SimplePath.class, SimpleTemplate.class);
        register(TypeCategory.SET, Expression.class, SimplePath.class, SimpleTemplate.class);
        register(TypeCategory.LIST, Expression.class, SimplePath.class, SimpleTemplate.class);
        register(TypeCategory.MAP, Expression.class, SimplePath.class, SimpleTemplate.class);

        register(TypeCategory.CUSTOM, Expression.class, Path.class, SimpleTemplate.class);
        register(TypeCategory.ENTITY, Expression.class, Path.class, SimpleTemplate.class);
    }

    public Type getTemplateType(Type type, EntityType model, boolean raw){
        return getTemplateType(type, model, raw, false, false);
    }

    public Type getTemplateType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        return getQueryType(templateTypes, type, model, raw, rawParameters, extend);
    }

    public Type getExprType(Type type, EntityType model, boolean raw){
        return getExprType(type, model, raw, false, false);
    }

    public Type getExprType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        if (queryTypes.containsKey(type.getFullName())){
            return queryTypes.get(type.getFullName());
        }else{
            return getQueryType(exprTypes, type, model, raw, rawParameters, extend);
        }
    }

    public Type getPathType(Type type, EntityType model, boolean raw){
        return getPathType(type, model, raw, false, false);
    }

    public Type getPathType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        if (queryTypes.containsKey(type.getFullName())){
            return queryTypes.get(type.getFullName());
        }else{
            return getQueryType(pathTypes, type, model, raw, rawParameters, extend);
        }
    }

    private Type getQueryType(Map<TypeCategory, Type> types, Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        Type exprType = types.get(type.getCategory());
        return getQueryType(type, model, exprType, raw, rawParameters, extend);
    }

    public Type getQueryType(Type type, EntityType model, Type exprType, boolean raw, boolean rawParameters, boolean extend){
        TypeCategory category = type.getCategory();
        if (raw && category != TypeCategory.ENTITY && category != TypeCategory.CUSTOM){
            return exprType;

        }else if (category == TypeCategory.STRING || category == TypeCategory.BOOLEAN){
            return exprType;

        }else{
            if (rawParameters){
                type = new SimpleType(type);
            }
            if (!type.isFinal() && extend){
                type = new TypeExtends(type);
            }
            return new SimpleType(exprType, type);

        }
    }

    @SuppressWarnings("unchecked")
    public void register(TypeCategory category,
            @Nullable Class<? extends Expression> expr,
            @Nullable Class<? extends Path> path,
            @Nullable Class<? extends TemplateExpression> template){
        if (expr != null){
            exprTypes.put(category, new ClassType(expr));
        }
        if (path != null){
            pathTypes.put(category, new ClassType(path));
        }
        if (template != null){
            templateTypes.put(category, new ClassType(template));
        }
    }

    public void register(Type type, Type queryType){
        queryTypes.put(type.getFullName(), queryType);
    }

    public boolean isRegistered(Type type){
        return queryTypes.containsKey(type.getFullName());
    }
}
