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
 * SPrice is a Querydsl query type for SPrice
 */
public class SPrice extends RelationalPathBase<SPrice> implements RelationalPath<SPrice> {

    private static final long serialVersionUID = -1644550752;

    public static final SPrice price = new SPrice("PRICE");

    public final NumberPath<Long> amount = createNumber("AMOUNT", Long.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final PrimaryKey<SPrice> sql100819184437800 = createPrimaryKey(id);

    public final ForeignKey<SItem> fk49cc129a549aeb0 = new ForeignKey<SItem>(this, productId, "ID");

    public final ForeignKey<SCatalogPrice> _fke4eb7d639d62434f = new ForeignKey<SCatalogPrice>(this, id, "PRICES_ID");

    public SPrice(String variable) {
        super(SPrice.class, forVariable(variable), null, "PRICE");
    }

    public SPrice(BeanPath<? extends SPrice> entity) {
        super(entity.getType(), entity.getMetadata(), null, "PRICE");
    }

    public SPrice(PathMetadata<?> metadata) {
        super(SPrice.class, metadata, null, "PRICE");
    }

}

