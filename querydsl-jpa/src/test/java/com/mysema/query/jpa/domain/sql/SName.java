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
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SName is a Querydsl query type for SName
 */
public class SName extends RelationalPathBase<SName> implements RelationalPath<SName> {

    private static final long serialVersionUID = 501063508;

    public static final SName name = new SName("NAME");

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath lastname = createString("LASTNAME");

    public final StringPath nickname = createString("NICKNAME");

    public final PrimaryKey<SName> sql100819184435150 = createPrimaryKey(id);

    public final ForeignKey<SCustomer> _fk27fbe3fe4707a44 = new ForeignKey<SCustomer>(this, id, "NAME_ID");

    public SName(String variable) {
        super(SName.class, forVariable(variable), null, "NAME");
    }

    public SName(BeanPath<? extends SName> entity) {
        super(entity.getType(), entity.getMetadata(), null, "NAME");
    }

    public SName(PathMetadata<?> metadata) {
        super(SName.class, metadata, null, "NAME");
    }

}

