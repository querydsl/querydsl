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
package com.querydsl.jdo.test.domain.sql;

import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SStore is a Querydsl querydsl type for SStore
 */
//@Table(value="STORE")
public class SStore extends RelationalPathBase<SStore> {

    private static final long serialVersionUID = -1302810257;

    public static final SStore store = new SStore("STORE");

    public final StringPath name = createString("NAME");

    public final NumberPath<Long> storeId = createNumber("STORE_ID", Long.class);

    public final PrimaryKey<SStore> sysIdx51 = createPrimaryKey(storeId);

    public final ForeignKey<SStoreProductsbyname> _storeProductsbynameFk1 = new ForeignKey<SStoreProductsbyname>(this, storeId, "STORE_ID_OID");

    public final ForeignKey<SStoreProducts> _storeProductsFk1 = new ForeignKey<SStoreProducts>(this, storeId, "STORE_ID_OID");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable), "", "STORE");
    }

    public SStore(BeanPath<? extends SStore> entity) {
        super(entity.getType(),entity.getMetadata(), "", "STORE");
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata, "", "STORE");
    }

}

