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

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SAccount is a Querydsl query type for SAccount
 */
public class SAccount extends RelationalPathBase<SAccount> {

    private static final long serialVersionUID = -727563068;

    public static final SAccount account = new SAccount("ACCOUNT");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> ownerI = createNumber("OWNER_I", Long.class);

    public final StringPath somedata = createString("SOMEDATA");

    public final PrimaryKey<SAccount> sql100819184429820 = createPrimaryKey(id);

    public final ForeignKey<SPerson> fk1d0c220d257b5f1c = new ForeignKey<SPerson>(this, ownerI, "I");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable), null, "ACCOUNT");
    }

    public SAccount(BeanPath<? extends SAccount> entity) {
        super(entity.getType(), entity.getMetadata(), null, "ACCOUNT");
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata, null, "ACCOUNT");
    }

}

