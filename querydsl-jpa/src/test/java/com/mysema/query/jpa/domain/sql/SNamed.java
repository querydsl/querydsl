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
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SNamed is a Querydsl query type for SNamed
 */
public class SNamed extends RelationalPathBase<SNamed> implements RelationalPath<SNamed> {

    private static final long serialVersionUID = -1646900336;

    public static final SNamed named = new SNamed("NAMED");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SNamed> sql100819184435830 = createPrimaryKey(id);

    public SNamed(String variable) {
        super(SNamed.class, forVariable(variable), null, "NAMED");
    }

    public SNamed(BeanPath<? extends SNamed> entity) {
        super(entity.getType(), entity.getMetadata(), null, "NAMED");
    }

    public SNamed(PathMetadata<?> metadata) {
        super(SNamed.class, metadata, null, "NAMED");
    }

}

