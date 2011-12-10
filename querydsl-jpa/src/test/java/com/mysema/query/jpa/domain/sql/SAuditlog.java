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
 * SAuditlog is a Querydsl query type for SAuditlog
 */
public class SAuditlog extends RelationalPathBase<SAuditlog> {

    private static final long serialVersionUID = 2033602002;

    public static final SAuditlog auditlog = new SAuditlog("AUDITLOG");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> itemId = createNumber("ITEM_ID", Long.class);

    public final PrimaryKey<SAuditlog> sql100819184430350 = createPrimaryKey(id);

    public final ForeignKey<SItem> fk3e07a1891bee4d44 = new ForeignKey<SItem>(this, itemId, "ID");

    public SAuditlog(String variable) {
        super(SAuditlog.class, forVariable(variable), null, "AUDITLOG");
    }

    public SAuditlog(BeanPath<? extends SAuditlog> entity) {
        super(entity.getType(), entity.getMetadata(), null, "AUDITLOG");
    }

    public SAuditlog(PathMetadata<?> metadata) {
        super(SAuditlog.class, metadata, null, "AUDITLOG");
    }
    
}

