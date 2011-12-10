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
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSyssequences is a Querydsl query type for SSyssequences
 */
public class SSyssequences extends com.mysema.query.sql.RelationalPathBase<SSyssequences> {

    private static final long serialVersionUID = -1065496554;

    public static final SSyssequences syssequences = new SSyssequences("SYSSEQUENCES");

    public final NumberPath<Long> currentvalue = createNumber("CURRENTVALUE", Long.class);

    public final StringPath cycleoption = createString("CYCLEOPTION");

    public final NumberPath<Long> increment = createNumber("INCREMENT", Long.class);

    public final NumberPath<Long> maximumvalue = createNumber("MAXIMUMVALUE", Long.class);

    public final NumberPath<Long> minimumvalue = createNumber("MINIMUMVALUE", Long.class);

    public final StringPath schemaid = createString("SCHEMAID");

    public final SimplePath<Object> sequencedatatype = createSimple("SEQUENCEDATATYPE", Object.class);

    public final StringPath sequenceid = createString("SEQUENCEID");

    public final StringPath sequencename = createString("SEQUENCENAME");

    public final NumberPath<Long> startvalue = createNumber("STARTVALUE", Long.class);

    public SSyssequences(String variable) {
        super(SSyssequences.class, forVariable(variable), "SYS", "SYSSEQUENCES");
    }

    public SSyssequences(Path<? extends SSyssequences> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSSEQUENCES");
    }

    public SSyssequences(PathMetadata<?> metadata) {
        super(SSyssequences.class, metadata, "SYS", "SYSSEQUENCES");
    }

}

