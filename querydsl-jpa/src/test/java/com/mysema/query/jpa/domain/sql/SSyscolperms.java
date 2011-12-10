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
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSyscolperms is a Querydsl query type for SSyscolperms
 */
public class SSyscolperms extends com.mysema.query.sql.RelationalPathBase<SSyscolperms> {

    private static final long serialVersionUID = -626716097;

    public static final SSyscolperms syscolperms = new SSyscolperms("SYSCOLPERMS");

    public final StringPath colpermsid = createString("COLPERMSID");

    public final SimplePath<Object> columns = createSimple("COLUMNS", Object.class);

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath tableid = createString("TABLEID");

    public final StringPath type = createString("TYPE");

    public SSyscolperms(String variable) {
        super(SSyscolperms.class, forVariable(variable), "SYS", "SYSCOLPERMS");
    }

    public SSyscolperms(Path<? extends SSyscolperms> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSCOLPERMS");
    }

    public SSyscolperms(PathMetadata<?> metadata) {
        super(SSyscolperms.class, metadata, "SYS", "SYSCOLPERMS");
    }

}

