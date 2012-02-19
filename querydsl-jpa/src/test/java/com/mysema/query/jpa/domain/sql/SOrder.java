package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SOrder is a Querydsl query type for SOrder
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SOrder extends com.mysema.query.sql.RelationalPathBase<SOrder> {

    private static final long serialVersionUID = -114696963;

    public static final SOrder order = new SOrder("ORDER_");

    public final NumberPath<Integer> customerId = createNumber("CUSTOMER_ID", Integer.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Short> paid = createNumber("PAID", Short.class);

    public final com.mysema.query.sql.PrimaryKey<SOrder> sql120219232327190 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCustomer> fkc3df62d1b29c27bc = createForeignKey(customerId, "ID");

    public final com.mysema.query.sql.ForeignKey<SCustomer> _fk600e7c419cc457f1 = createInvForeignKey(id, "CURRENTORDER_ID");

    public SOrder(String variable) {
        super(SOrder.class, forVariable(variable), "APP", "ORDER_");
    }

    public SOrder(Path<? extends SOrder> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "ORDER_");
    }

    public SOrder(PathMetadata<?> metadata) {
        super(SOrder.class, metadata, "APP", "ORDER_");
    }

}

