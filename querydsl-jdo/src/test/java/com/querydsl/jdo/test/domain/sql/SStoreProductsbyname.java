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
 * SStoreProductsbyname is a Querydsl querydsl type for SStoreProductsbyname
 */
//@Table(value="STORE_PRODUCTSBYNAME")
public class SStoreProductsbyname extends RelationalPathBase<SStoreProductsbyname> {

    private static final long serialVersionUID = 764053781;

    public static final SStoreProductsbyname storeProductsbyname = new SStoreProductsbyname("STORE_PRODUCTSBYNAME");

    public final StringPath key = createString("KEY");

    public final NumberPath<Long> productIdVid = createNumber("PRODUCT_ID_VID", Long.class);

    public final NumberPath<Long> storeIdOid = createNumber("STORE_ID_OID", Long.class);

    public final PrimaryKey<SStoreProductsbyname> sysIdx53 = createPrimaryKey(key, storeIdOid);

    public final ForeignKey<SStore> storeProductsbynameFk1 = new ForeignKey<SStore>(this, storeIdOid, "STORE_ID");

    public final ForeignKey<SProduct> storeProductsbynameFk2 = new ForeignKey<SProduct>(this, productIdVid, "PRODUCT_ID");

    public SStoreProductsbyname(String variable) {
        super(SStoreProductsbyname.class, forVariable(variable), "", "STORE_PRODUCTSBYNAME");
    }

    public SStoreProductsbyname(BeanPath<? extends SStoreProductsbyname> entity) {
        super(entity.getType(),entity.getMetadata(), "", "STORE_PRODUCTSBYNAME");
    }

    public SStoreProductsbyname(PathMetadata<?> metadata) {
        super(SStoreProductsbyname.class, metadata, "", "STORE_PRODUCTSBYNAME");
    }

}

