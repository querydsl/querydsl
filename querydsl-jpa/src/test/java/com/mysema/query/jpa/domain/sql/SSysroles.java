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
 * SSysroles is a Querydsl query type for SSysroles
 */
public class SSysroles extends com.mysema.query.sql.RelationalPathBase<SSysroles> {

    private static final long serialVersionUID = -1697898495;

    public static final SSysroles sysroles = new SSysroles("SYSROLES");

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath isdef = createString("ISDEF");

    public final StringPath roleid = createString("ROLEID");

    public final StringPath uuid = createString("UUID");

    public final StringPath withadminoption = createString("WITHADMINOPTION");

    public SSysroles(String variable) {
        super(SSysroles.class, forVariable(variable), "SYS", "SYSROLES");
    }

    public SSysroles(Path<? extends SSysroles> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSROLES");
    }

    public SSysroles(PathMetadata<?> metadata) {
        super(SSysroles.class, metadata, "SYS", "SYSROLES");
    }

}

