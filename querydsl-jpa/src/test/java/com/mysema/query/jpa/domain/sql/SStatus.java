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
 * SStatus is a Querydsl query type for SStatus
 */
public class SStatus extends RelationalPathBase<SStatus> implements RelationalPath<SStatus> {

    private static final long serialVersionUID = 646047355;

    public static final SStatus status = new SStatus("STATUS");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SStatus> sql100819184438880 = createPrimaryKey(id);

    public final ForeignKey<SItem> _fk22ef33eedeba64 = new ForeignKey<SItem>(this, id, "STATUS_ID");

    public final ForeignKey<SItem> _fk22ef33bb4e150b = new ForeignKey<SItem>(this, id, "CURRENTSTATUS_ID");

    public SStatus(String variable) {
        super(SStatus.class, forVariable(variable), null, "STATUS");
    }

    public SStatus(BeanPath<? extends SStatus> entity) {
        super(entity.getType(), entity.getMetadata(), null, "STATUS");
    }

    public SStatus(PathMetadata<?> metadata) {
        super(SStatus.class, metadata, null, "STATUS");
    }

}

