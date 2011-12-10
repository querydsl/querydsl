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
import com.mysema.query.types.path.StringPath;


/**
 * SSysschemas is a Querydsl query type for SSysschemas
 */
public class SSysschemas extends com.mysema.query.sql.RelationalPathBase<SSysschemas> {

    private static final long serialVersionUID = 947375926;

    public static final SSysschemas sysschemas = new SSysschemas("SYSSCHEMAS");

    public final StringPath authorizationid = createString("AUTHORIZATIONID");

    public final StringPath schemaid = createString("SCHEMAID");

    public final StringPath schemaname = createString("SCHEMANAME");

    public SSysschemas(String variable) {
        super(SSysschemas.class, forVariable(variable), "SYS", "SYSSCHEMAS");
    }

    public SSysschemas(Path<? extends SSysschemas> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSSCHEMAS");
    }

    public SSysschemas(PathMetadata<?> metadata) {
        super(SSysschemas.class, metadata, "SYS", "SYSSCHEMAS");
    }

}

