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
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SItemStatuschange is a Querydsl query type for SItemStatuschange
 */
public class SItemStatuschange extends RelationalPathBase<SItemStatuschange> {

    private static final long serialVersionUID = 675000350;

    public static final SItemStatuschange itemStatuschange = new SItemStatuschange("ITEM_STATUSCHANGE");

    public final NumberPath<Long> itemId = createNumber("ITEM_ID", Long.class);

    public final NumberPath<Long> statuschangesId = createNumber("STATUSCHANGES_ID", Long.class);

    public final ForeignKey<SItem> fkc2c9ebee9e7e0323 = new ForeignKey<SItem>(this, itemId, "ID");

    public final ForeignKey<SStatuschange> fkc2c9ebee2f721e35 = new ForeignKey<SStatuschange>(this, statuschangesId, "ID");

    public SItemStatuschange(String variable) {
        super(SItemStatuschange.class, forVariable(variable), null, "ITEM_STATUSCHANGE");
    }

    public SItemStatuschange(BeanPath<? extends SItemStatuschange> entity) {
        super(entity.getType(), entity.getMetadata(), null, "ITEM_STATUSCHANGE");
    }

    public SItemStatuschange(PathMetadata<?> metadata) {
        super(SItemStatuschange.class, metadata, null, "ITEM_STATUSCHANGE");
    }

}

