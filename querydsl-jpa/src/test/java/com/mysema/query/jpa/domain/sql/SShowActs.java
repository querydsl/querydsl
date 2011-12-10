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
import com.mysema.query.types.path.StringPath;


/**
 * SShowActs is a Querydsl query type for SShowActs
 */
public class SShowActs extends RelationalPathBase<SShowActs> implements RelationalPath<SShowActs> {

    private static final long serialVersionUID = 718125831;

    public static final SShowActs showActs = new SShowActs("SHOW_ACTS");

    public final StringPath actsKey = createString("ACTS_KEY");

    public final StringPath element = createString("ELEMENT");

    public final NumberPath<Integer> showId = createNumber("SHOW_ID", Integer.class);

    public final PrimaryKey<SShowActs> sql100819184438340 = createPrimaryKey(actsKey, showId);

    public final ForeignKey<SShow> fk5f6ee0319084d04 = new ForeignKey<SShow>(this, showId, "ID");

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable), null, "SHOW_ACTS");
    }

    public SShowActs(BeanPath<? extends SShowActs> entity) {
        super(entity.getType(), entity.getMetadata(), null, "SHOW_ACTS");
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata, null, "SHOW_ACTS");
    }

}

