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
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSystriggers is a Querydsl query type for SSystriggers
 */
public class SSystriggers extends com.mysema.query.sql.RelationalPathBase<SSystriggers> {

    private static final long serialVersionUID = 1504647671;

    public static final SSystriggers systriggers = new SSystriggers("SYSTRIGGERS");

    public final StringPath actionstmtid = createString("ACTIONSTMTID");

    public final DateTimePath<java.sql.Timestamp> creationtimestamp = createDateTime("CREATIONTIMESTAMP", java.sql.Timestamp.class);

    public final StringPath event = createString("EVENT");

    public final StringPath firingtime = createString("FIRINGTIME");

    public final StringPath newreferencingname = createString("NEWREFERENCINGNAME");

    public final StringPath oldreferencingname = createString("OLDREFERENCINGNAME");

    public final SimplePath<Object> referencedcolumns = createSimple("REFERENCEDCOLUMNS", Object.class);

    public final BooleanPath referencingnew = createBoolean("REFERENCINGNEW");

    public final BooleanPath referencingold = createBoolean("REFERENCINGOLD");

    public final StringPath schemaid = createString("SCHEMAID");

    public final StringPath state = createString("STATE");

    public final StringPath tableid = createString("TABLEID");

    public final StringPath triggerdefinition = createString("TRIGGERDEFINITION");

    public final StringPath triggerid = createString("TRIGGERID");

    public final StringPath triggername = createString("TRIGGERNAME");

    public final StringPath type = createString("TYPE");

    public final StringPath whenstmtid = createString("WHENSTMTID");

    public SSystriggers(String variable) {
        super(SSystriggers.class, forVariable(variable), "SYS", "SYSTRIGGERS");
    }

    public SSystriggers(Path<? extends SSystriggers> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSTRIGGERS");
    }

    public SSystriggers(PathMetadata<?> metadata) {
        super(SSystriggers.class, metadata, "SYS", "SYSTRIGGERS");
    }

}

