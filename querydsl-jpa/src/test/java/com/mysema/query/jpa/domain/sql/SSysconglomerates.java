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

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSysconglomerates is a Querydsl query type for SSysconglomerates
 */
public class SSysconglomerates extends com.mysema.query.sql.RelationalPathBase<SSysconglomerates> {

    private static final long serialVersionUID = 2134368727;

    public static final SSysconglomerates sysconglomerates = new SSysconglomerates("SYSCONGLOMERATES");

    public final StringPath conglomerateid = createString("CONGLOMERATEID");

    public final StringPath conglomeratename = createString("CONGLOMERATENAME");

    public final NumberPath<Long> conglomeratenumber = createNumber("CONGLOMERATENUMBER", Long.class);

    public final SimplePath<Object> descriptor = createSimple("DESCRIPTOR", Object.class);

    public final BooleanPath isconstraint = createBoolean("ISCONSTRAINT");

    public final BooleanPath isindex = createBoolean("ISINDEX");

    public final StringPath schemaid = createString("SCHEMAID");

    public final StringPath tableid = createString("TABLEID");

    public SSysconglomerates(String variable) {
        super(SSysconglomerates.class, forVariable(variable), "SYS", "SYSCONGLOMERATES");
    }

    public SSysconglomerates(Path<? extends SSysconglomerates> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSCONGLOMERATES");
    }

    public SSysconglomerates(PathMetadata<?> metadata) {
        super(SSysconglomerates.class, metadata, "SYS", "SYSCONGLOMERATES");
    }

}

