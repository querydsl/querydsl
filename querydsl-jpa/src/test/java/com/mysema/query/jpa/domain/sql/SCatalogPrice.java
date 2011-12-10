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
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SCatalogPrice is a Querydsl query type for SCatalogPrice
 */
public class SCatalogPrice extends RelationalPathBase<SCatalogPrice> {

    private static final long serialVersionUID = -1748773767;

    public static final SCatalogPrice catalogPrice = new SCatalogPrice("CATALOG_PRICE");

    public final NumberPath<Integer> catalogId = createNumber("CATALOG_ID", Integer.class);

    public final NumberPath<Long> pricesId = createNumber("PRICES_ID", Long.class);

    public final PrimaryKey<SCatalogPrice> sql100819184431880 = createPrimaryKey(catalogId, pricesId);

    public final ForeignKey<SPrice> fke4eb7d639d62434f = new ForeignKey<SPrice>(this, pricesId, "ID");

    public final ForeignKey<SCatalog> fke4eb7d63f28fe670 = new ForeignKey<SCatalog>(this, catalogId, "ID");

    public SCatalogPrice(String variable) {
        super(SCatalogPrice.class, forVariable(variable), null, "CATALOG_PRICE");
    }

    public SCatalogPrice(BeanPath<? extends SCatalogPrice> entity) {
        super(entity.getType(), entity.getMetadata(), null, "CATALOG_PRICE");
    }

    public SCatalogPrice(PathMetadata<?> metadata) {
        super(SCatalogPrice.class, metadata, null, "CATALOG_PRICE");
    }

}

