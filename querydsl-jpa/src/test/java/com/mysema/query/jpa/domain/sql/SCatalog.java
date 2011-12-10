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
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;


/**
 * SCatalog is a Querydsl query type for SCatalog
 */
public class SCatalog extends RelationalPathBase<SCatalog> {

    private static final long serialVersionUID = 1005460144;

    public static final SCatalog catalog = new SCatalog("CATALOG");

    public final DatePath<java.sql.Date> effectivedate = createDate("EFFECTIVEDATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final PrimaryKey<SCatalog> sql100819184431520 = createPrimaryKey(id);

    public final ForeignKey<SCatalogPrice> _fke4eb7d63f28fe670 = new ForeignKey<SCatalogPrice>(this, id, "CATALOG_ID");

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable), null, "CATALOG");
    }

    public SCatalog(BeanPath<? extends SCatalog> entity) {
        super(entity.getType(), entity.getMetadata(), null, "CATALOG");
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata, null, "CATALOG");
    }

}

