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
 * SSysroutineperms is a Querydsl query type for SSysroutineperms
 */
public class SSysroutineperms extends com.mysema.query.sql.RelationalPathBase<SSysroutineperms> {

    private static final long serialVersionUID = 446537595;

    public static final SSysroutineperms sysroutineperms = new SSysroutineperms("SYSROUTINEPERMS");

    public final StringPath aliasid = createString("ALIASID");

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantoption = createString("GRANTOPTION");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath routinepermsid = createString("ROUTINEPERMSID");

    public SSysroutineperms(String variable) {
        super(SSysroutineperms.class, forVariable(variable), "SYS", "SYSROUTINEPERMS");
    }

    public SSysroutineperms(Path<? extends SSysroutineperms> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSROUTINEPERMS");
    }

    public SSysroutineperms(PathMetadata<?> metadata) {
        super(SSysroutineperms.class, metadata, "SYS", "SYSROUTINEPERMS");
    }

}

