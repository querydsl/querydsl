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
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SStoreCustomer is a Querydsl query type for SStoreCustomer
 */
public class SStoreCustomer extends RelationalPathBase<SStoreCustomer> implements RelationalPath<SStoreCustomer> {

    private static final long serialVersionUID = 1667012918;

    public static final SStoreCustomer storeCustomer = new SStoreCustomer("STORE_CUSTOMER");

    public final NumberPath<Integer> customersId = createNumber("CUSTOMERS_ID", Integer.class);

    public final NumberPath<Long> storeId = createNumber("STORE_ID", Long.class);

    public final ForeignKey<SStore> fk808055bc828daef0 = new ForeignKey<SStore>(this, storeId, "ID");

    public final ForeignKey<SCustomer> fk808055bcf27d6c8d = new ForeignKey<SCustomer>(this, customersId, "ID");

    public SStoreCustomer(String variable) {
        super(SStoreCustomer.class, forVariable(variable), null, "STORE_CUSTOMER");
    }

    public SStoreCustomer(BeanPath<? extends SStoreCustomer> entity) {
        super(entity.getType(), entity.getMetadata(), null, "STORE_CUSTOMER");
    }

    public SStoreCustomer(PathMetadata<?> metadata) {
        super(SStoreCustomer.class, metadata, null, "STORE_CUSTOMER");
    }

}

