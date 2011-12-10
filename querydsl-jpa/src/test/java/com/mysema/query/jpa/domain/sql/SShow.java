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
 * SShow is a Querydsl query type for SShow
 */
public class SShow extends RelationalPathBase<SShow> implements RelationalPath<SShow> {

    private static final long serialVersionUID = 501219270;

    public static final SShow show = new SShow("SHOW");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final PrimaryKey<SShow> sql100819184438080 = createPrimaryKey(id);

    public final ForeignKey<SShowActs> _fk5f6ee0319084d04 = new ForeignKey<SShowActs>(this, id, "SHOW_ID");

    public SShow(String variable) {
        super(SShow.class, forVariable(variable), null, "SHOW");
    }

    public SShow(BeanPath<? extends SShow> entity) {
        super(entity.getType(), entity.getMetadata(), null, "SHOW");
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata, null, "SHOW");
    }

}

