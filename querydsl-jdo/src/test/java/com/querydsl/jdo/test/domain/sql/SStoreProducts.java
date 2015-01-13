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
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SStoreProducts is a Querydsl querydsl type for SStoreProducts
 */
//@Table(value="STORE_PRODUCTS")
public class SStoreProducts extends RelationalPathBase<SStoreProducts> {

    private static final long serialVersionUID = 1019873267;

    public static final SStoreProducts storeProducts = new SStoreProducts("STORE_PRODUCTS");

    public final NumberPath<Integer> idx = createNumber("IDX", Integer.class);

    public final NumberPath<Long> productIdEid = createNumber("PRODUCT_ID_EID", Long.class);

    public final NumberPath<Long> storeIdOid = createNumber("STORE_ID_OID", Long.class);

    public final PrimaryKey<SStoreProducts> sysIdx55 = createPrimaryKey(idx, storeIdOid);

    public final ForeignKey<SProduct> storeProductsFk2 = new ForeignKey<SProduct>(this, productIdEid, "PRODUCT_ID");

    public final ForeignKey<SStore> storeProductsFk1 = new ForeignKey<SStore>(this, storeIdOid, "STORE_ID");

    public SStoreProducts(String variable) {
        super(SStoreProducts.class, forVariable(variable), "", "STORE_PRODUCTS");
    }

    public SStoreProducts(BeanPath<? extends SStoreProducts> entity) {
        super(entity.getType(),entity.getMetadata(), "", "STORE_PRODUCTS");
    }

    public SStoreProducts(PathMetadata<?> metadata) {
        super(SStoreProducts.class, metadata, "", "STORE_PRODUCTS");
    }

}

