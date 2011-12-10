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
 * SSystableperms is a Querydsl query type for SSystableperms
 */
public class SSystableperms extends com.mysema.query.sql.RelationalPathBase<SSystableperms> {

    private static final long serialVersionUID = 4684209;

    public static final SSystableperms systableperms = new SSystableperms("SYSTABLEPERMS");

    public final StringPath deletepriv = createString("DELETEPRIV");

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath insertpriv = createString("INSERTPRIV");

    public final StringPath referencespriv = createString("REFERENCESPRIV");

    public final StringPath selectpriv = createString("SELECTPRIV");

    public final StringPath tableid = createString("TABLEID");

    public final StringPath tablepermsid = createString("TABLEPERMSID");

    public final StringPath triggerpriv = createString("TRIGGERPRIV");

    public final StringPath updatepriv = createString("UPDATEPRIV");

    public SSystableperms(String variable) {
        super(SSystableperms.class, forVariable(variable), "SYS", "SYSTABLEPERMS");
    }

    public SSystableperms(Path<? extends SSystableperms> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSTABLEPERMS");
    }

    public SSystableperms(PathMetadata<?> metadata) {
        super(SSystableperms.class, metadata, "SYS", "SYSTABLEPERMS");
    }

}

