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
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SFoo is a Querydsl query type for SFoo
 */
public class SFoo extends RelationalPathBase<SFoo> {

    private static final long serialVersionUID = 1401629405;

    public static final SFoo foo = new SFoo("FOO");

    public final StringPath bar = createString("BAR");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final DatePath<java.sql.Date> startdate = createDate("STARTDATE", java.sql.Date.class);

    public final PrimaryKey<SFoo> sql100819184433460 = createPrimaryKey(id);

    public SFoo(String variable) {
        super(SFoo.class, forVariable(variable), null, "FOO");
    }

    public SFoo(BeanPath<? extends SFoo> entity) {
        super(entity.getType(), entity.getMetadata(), null, "FOO");
    }

    public SFoo(PathMetadata<?> metadata) {
        super(SFoo.class, metadata, null, "FOO");
    }

}

