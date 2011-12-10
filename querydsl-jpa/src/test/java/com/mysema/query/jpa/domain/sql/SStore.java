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


/**
 * SStore is a Querydsl query type for SStore
 */
public class SStore extends RelationalPathBase<SStore> implements RelationalPath<SStore> {

    private static final long serialVersionUID = -1641714376;

    public static final SStore store = new SStore("STORE");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> locationId = createNumber("LOCATION_ID", Long.class);

    public final PrimaryKey<SStore> sql100819184439380 = createPrimaryKey(id);

    public final ForeignKey<SLocation> fk4c808c12adf2d04 = new ForeignKey<SLocation>(this, locationId, "ID");

    public final ForeignKey<SStoreCustomer> _fk808055bc828daef0 = new ForeignKey<SStoreCustomer>(this, id, "STORE_ID");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable), null, "STORE");
    }

    public SStore(BeanPath<? extends SStore> entity) {
        super(entity.getType(), entity.getMetadata(), null, "STORE");
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata, null, "STORE");
    }

}

