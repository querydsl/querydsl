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
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SInheritedproperties is a Querydsl query type for SInheritedproperties
 */
public class SInheritedproperties extends RelationalPathBase<SInheritedproperties> {

    private static final long serialVersionUID = -2093604956;

    public static final SInheritedproperties inheritedproperties = new SInheritedproperties("INHERITEDPROPERTIES");

    public final StringPath classproperty = createString("CLASSPROPERTY");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath stringassimple = createString("STRINGASSIMPLE");

    public final StringPath superclassproperty = createString("SUPERCLASSPROPERTY");

    public final PrimaryKey<SInheritedproperties> sql100819184434080 = createPrimaryKey(id);

    public SInheritedproperties(String variable) {
        super(SInheritedproperties.class, forVariable(variable), null, "INHERITEDPROPERTIES");
    }

    public SInheritedproperties(BeanPath<? extends SInheritedproperties> entity) {
        super(entity.getType(), entity.getMetadata(), null, "INHERITEDPROPERTIES");
    }

    public SInheritedproperties(PathMetadata<?> metadata) {
        super(SInheritedproperties.class, metadata, null, "INHERITEDPROPERTIES");
    }

}

