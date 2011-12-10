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
import com.mysema.query.types.path.StringPath;


/**
 * SDocument is a Querydsl query type for SDocument
 */
public class SDocument extends RelationalPathBase<SDocument> {

    private static final long serialVersionUID = 1919248740;

    public static final SDocument document = new SDocument("DOCUMENT");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final DatePath<java.sql.Date> validto = createDate("VALIDTO", java.sql.Date.class);

    public final PrimaryKey<SDocument> sql100819184432950 = createPrimaryKey(id);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable), null, "DOCUMENT");
    }

    public SDocument(BeanPath<? extends SDocument> entity) {
        super(entity.getType(), entity.getMetadata(), null, "DOCUMENT");
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata, null, "DOCUMENT");
    }

}

