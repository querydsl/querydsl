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
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SPerson is a Querydsl query type for SPerson
 */
public class SPerson extends RelationalPathBase<SPerson> implements RelationalPath<SPerson> {

    private static final long serialVersionUID = 546812382;

    public static final SPerson person = new SPerson("PERSON");

    public final DatePath<java.sql.Date> birthday = createDate("BIRTHDAY", java.sql.Date.class);

    public final NumberPath<Long> i = createNumber("I", Long.class);

    public final StringPath name = createString("NAME");

    public final NumberPath<Long> nationalityId = createNumber("NATIONALITY_ID", Long.class);

    public final NumberPath<Long> pidId = createNumber("PID_ID", Long.class);

    public final PrimaryKey<SPerson> sql100819184436900 = createPrimaryKey(i);

    public final ForeignKey<SNationality> fk8e488775e9d94490 = new ForeignKey<SNationality>(this, nationalityId, "ID");

    public final ForeignKey<SPersonid> fk8e48877578234709 = new ForeignKey<SPersonid>(this, pidId, "ID");

    public final ForeignKey<SAccount> _fk1d0c220d257b5f1c = new ForeignKey<SAccount>(this, i, "OWNER_I");

    public SPerson(String variable) {
        super(SPerson.class, forVariable(variable), null, "PERSON");
    }

    public SPerson(BeanPath<? extends SPerson> entity) {
        super(entity.getType(), entity.getMetadata(), null, "PERSON");
    }

    public SPerson(PathMetadata<?> metadata) {
        super(SPerson.class, metadata, null, "PERSON");
    }
    
}

