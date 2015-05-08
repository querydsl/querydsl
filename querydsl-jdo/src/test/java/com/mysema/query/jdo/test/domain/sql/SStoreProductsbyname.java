/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.mysema.query.jdo.test.domain.sql;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SStoreProductsbyname is a Querydsl query type for SStoreProductsbyname
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

