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
 * SOrder is a Querydsl query type for SOrder
 */
public class SOrder extends RelationalPathBase<SOrder> implements RelationalPath<SOrder> {

    private static final long serialVersionUID = -1645479003;

    public static final SOrder order = new SOrder("ORDER_");

    public final NumberPath<Integer> customerId = createNumber("CUSTOMER_ID", Integer.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Short> paid = createNumber("PAID", Short.class);

    public final PrimaryKey<SOrder> sql100819184436320 = createPrimaryKey(id);

    public final ForeignKey<SCustomer> fk8b7256f15325d064 = new ForeignKey<SCustomer>(this, customerId, "ID");

    public final ForeignKey<SCustomer> _fk27fbe3fee803b049 = new ForeignKey<SCustomer>(this, id, "CURRENTORDER_ID");

    public SOrder(String variable) {
        super(SOrder.class, forVariable(variable), null, "ORDER");
    }

    public SOrder(BeanPath<? extends SOrder> entity) {
        super(entity.getType(), entity.getMetadata(), null, "ORDER");
    }

    public SOrder(PathMetadata<?> metadata) {
        super(SOrder.class, metadata, null, "ORDER");
    }

}

