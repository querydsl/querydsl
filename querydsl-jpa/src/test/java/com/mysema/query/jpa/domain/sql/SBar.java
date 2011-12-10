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

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;


/**
 * SBar is a Querydsl query type for SBar
 */
public class SBar extends RelationalPathBase<SBar> {

    private static final long serialVersionUID = 1401625130;

    public static final SBar bar = new SBar("BAR");

    public final DatePath<java.sql.Date> date = createDate("DATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final PrimaryKey<SBar> sql100819184430740 = createPrimaryKey(id);

    public SBar(String variable) {
        super(SBar.class, forVariable(variable), null, "BAR");
    }

    public SBar(BeanPath<? extends SBar> entity) {
        super(entity.getType(), entity.getMetadata(), null, "BAR");
    }

    public SBar(PathMetadata<?> metadata) {
        super(SBar.class, metadata, null, "BAR");
    }

}

