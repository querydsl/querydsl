package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SOrder is a Querydsl query type for SOrder
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SOrder extends com.mysema.query.sql.RelationalPathBase<SOrder> {

    private static final long serialVersionUID = -114696963;

    public static final SOrder order = new SOrder("order_");

    public final NumberPath<Integer> customerId = createNumber("customer_id", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath paid = createBoolean("paid");

    public final com.mysema.query.sql.PrimaryKey<SOrder> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCustomer> fkc3df62d1b29c27bc = createForeignKey(customerId, "id");

    public final com.mysema.query.sql.ForeignKey<SCustomer> order_CUSTOMERIDFK = createForeignKey(customerId, "id");

    public final com.mysema.query.sql.ForeignKey<SOrder_item> _order_item_OrderIDFK = createInvForeignKey(id, "Order_ID");

    public final com.mysema.query.sql.ForeignKey<SOrderDELIVEREDITEMINDICES2> _orderDELIVEREDITEMINDICESOrderIDFK = createInvForeignKey(id, "Order_ID");

    public final com.mysema.query.sql.ForeignKey<SCustomer> _customer_CURRENTORDERIDFK = createInvForeignKey(id, "currentOrder_id");

    public final com.mysema.query.sql.ForeignKey<SOrder_item> _fk1b5e8cbeb968f515 = createInvForeignKey(id, "order__id");

    public final com.mysema.query.sql.ForeignKey<SCustomer> _fk600e7c419cc457f1 = createInvForeignKey(id, "currentOrder_id");

    public final com.mysema.query.sql.ForeignKey<SOrderDeliveredItemIndices> _fk30cbd6611a4d2378 = createInvForeignKey(id, "Order_id");

    public SOrder(String variable) {
        super(SOrder.class, forVariable(variable), "null", "order_");
    }

    public SOrder(Path<? extends SOrder> path) {
        super(path.getType(), path.getMetadata(), "null", "order_");
    }

    public SOrder(PathMetadata<?> metadata) {
        super(SOrder.class, metadata, "null", "order_");
    }

}

