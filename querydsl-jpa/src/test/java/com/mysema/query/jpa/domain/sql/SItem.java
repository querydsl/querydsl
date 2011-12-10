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
import com.mysema.query.types.path.StringPath;


/**
 * SItem is a Querydsl query type for SItem
 */
public class SItem extends RelationalPathBase<SItem> {

    private static final long serialVersionUID = 500932572;

    public static final SItem item = new SItem("ITEM");

    public final NumberPath<Long> currentstatusId = createNumber("CURRENTSTATUS_ID", Long.class);

    public final StringPath dtype = createString("DTYPE");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> name = createNumber("NAME", Integer.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final NumberPath<Long> statusId = createNumber("STATUS_ID", Long.class);

    public final PrimaryKey<SItem> sql100819184434350 = createPrimaryKey(id);

    public final ForeignKey<SItem> fk22ef33a549aeb0 = new ForeignKey<SItem>(this, productId, "ID");

    public final ForeignKey<SStatus> fk22ef33eedeba64 = new ForeignKey<SStatus>(this, statusId, "ID");

    public final ForeignKey<SStatus> fk22ef33bb4e150b = new ForeignKey<SStatus>(this, currentstatusId, "ID");

    public final ForeignKey<SAuditlog> _fk3e07a1891bee4d44 = new ForeignKey<SAuditlog>(this, id, "ITEM_ID");

    public final ForeignKey<SItemStatuschange> _fkc2c9ebee9e7e0323 = new ForeignKey<SItemStatuschange>(this, id, "ITEM_ID");

    public final ForeignKey<SItem> _fk22ef33a549aeb0 = new ForeignKey<SItem>(this, id, "PRODUCT_ID");

    public final ForeignKey<SPrice> _fk49cc129a549aeb0 = new ForeignKey<SPrice>(this, id, "PRODUCT_ID");

    public SItem(String variable) {
        super(SItem.class, forVariable(variable), null, "ITEM");
    }

    public SItem(BeanPath<? extends SItem> entity) {
        super(entity.getType(), entity.getMetadata(), null, "ITEM");
    }

    public SItem(PathMetadata<?> metadata) {
        super(SItem.class, metadata, null, "ITEM");
    }

}

