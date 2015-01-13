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

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SProduct is a Querydsl querydsl type for SProduct
 */
//@Table(value="PRODUCT")
public class SProduct extends RelationalPathBase<SProduct> {

    private static final long serialVersionUID = -590374403;

    public static final SProduct product = new SProduct("PRODUCT");

    public final NumberPath<Integer> amount = createNumber("AMOUNT", Integer.class);

    public final DatePath<java.sql.Date> datefield = createDate("DATEFIELD", java.sql.Date.class);

    public final StringPath description = createString("DESCRIPTION");

    public final StringPath name = createString("NAME");

    public final NumberPath<Double> price = createNumber("PRICE", Double.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final DateTimePath<java.util.Date> publicationdate = createDateTime("PUBLICATIONDATE", java.util.Date.class);

    public final TimePath<java.sql.Time> timefield = createTime("TIMEFIELD", java.sql.Time.class);

    public final PrimaryKey<SProduct> sysIdx47 = createPrimaryKey(productId);

    public final ForeignKey<SStoreProducts> _storeProductsFk2 = new ForeignKey<SStoreProducts>(this, productId, "PRODUCT_ID_EID");

    public final ForeignKey<SBook> _bookFk1 = new ForeignKey<SBook>(this, productId, "BOOK_ID");

    public final ForeignKey<SStoreProductsbyname> _storeProductsbynameFk2 = new ForeignKey<SStoreProductsbyname>(this, productId, "PRODUCT_ID_VID");

    public SProduct(String variable) {
        super(SProduct.class, forVariable(variable), "", "PRODUCT");
        addMetadata();
    }

    public SProduct(BeanPath<? extends SProduct> entity) {
        super(entity.getType(),entity.getMetadata(), "", "PRODUCT");
        addMetadata();
    }

    public SProduct(PathMetadata<?> metadata) {
        super(SProduct.class, metadata, "", "PRODUCT");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("AMOUNT"));
        addMetadata(datefield, ColumnMetadata.named("DATEFIELD"));
        addMetadata(description, ColumnMetadata.named("DESCRIPTION"));
        addMetadata(name, ColumnMetadata.named("NAME"));
        addMetadata(price, ColumnMetadata.named("PRICE"));
        addMetadata(productId, ColumnMetadata.named("PRODUCT_ID"));
        addMetadata(publicationdate, ColumnMetadata.named("PUBLICATIONDATE"));
        addMetadata(timefield, ColumnMetadata.named("TIMEFIELD"));
    }
    
}

