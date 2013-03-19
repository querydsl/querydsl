/*
 * Copyright 2012, Mysema Ltd
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
package com.mysema.query.codegen;

import com.mysema.codegen.model.TypeCategory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
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
 * JavaTypeMappings defines mappings from {@link TypeCategory} instances to {@link Expression} types
 * 
 * @author tiwe
 *
 */
public class JavaTypeMappings extends TypeMappings {

    public JavaTypeMappings() {
        register(TypeCategory.STRING,     StringExpression.class,     StringPath.class,     StringTemplate.class);
        register(TypeCategory.BOOLEAN,    BooleanExpression.class,    BooleanPath.class,    BooleanTemplate.class);
        register(TypeCategory.COMPARABLE, ComparableExpression.class, ComparablePath.class, ComparableTemplate.class);
        register(TypeCategory.ENUM,       EnumExpression.class,       EnumPath.class,       EnumTemplate.class);
        register(TypeCategory.DATE,       DateExpression.class,       DatePath.class,       DateTemplate.class);
        register(TypeCategory.DATETIME,   DateTimeExpression.class,   DateTimePath.class,   DateTimeTemplate.class);
        register(TypeCategory.TIME,       TimeExpression.class,       TimePath.class,       TimeTemplate.class);
        register(TypeCategory.NUMERIC,    NumberExpression.class,     NumberPath.class,     NumberTemplate.class);
        register(TypeCategory.SIMPLE,     Expression.class,           SimplePath.class,     SimpleTemplate.class);

        register(TypeCategory.ARRAY,      Expression.class,           ArrayPath.class,      SimpleTemplate.class);
        register(TypeCategory.COLLECTION, Expression.class,           SimplePath.class,     SimpleTemplate.class);
        register(TypeCategory.SET,        Expression.class,           SimplePath.class,     SimpleTemplate.class);
        register(TypeCategory.LIST,       Expression.class,           SimplePath.class,     SimpleTemplate.class);
        register(TypeCategory.MAP,        Expression.class,           SimplePath.class,     SimpleTemplate.class);

        register(TypeCategory.CUSTOM,     Expression.class,           Path.class,           SimpleTemplate.class);
        register(TypeCategory.ENTITY,     Expression.class,           Path.class,           SimpleTemplate.class);
    }

    
}
