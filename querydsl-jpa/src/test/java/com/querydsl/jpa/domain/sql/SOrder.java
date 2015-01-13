package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BooleanPath;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SOrder is a Querydsl querydsl type for SOrder
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SOrder extends com.querydsl.sql.RelationalPathBase<SOrder> {

    private static final long serialVersionUID = 739361538;

    public static final SOrder order_ = new SOrder("order_");

    public final NumberPath<Integer> customerId = createNumber("customerId", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath paid = createBoolean("paid");

    public final com.querydsl.sql.PrimaryKey<SOrder> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SCustomer> fkc3df62d1b29c27bc = createForeignKey(customerId, "id");

    public final com.querydsl.sql.ForeignKey<SOrder_item> _fk1b5e8cbeb968f515 = createInvForeignKey(id, "order__id");

    public final com.querydsl.sql.ForeignKey<SCustomer> _fk600e7c419cc457f1 = createInvForeignKey(id, "currentOrder_id");

    public final com.querydsl.sql.ForeignKey<SOrderDeliveredItemIndices> _fk30cbd6611a4d2378 = createInvForeignKey(id, "Order_id");

    public final com.querydsl.sql.ForeignKey<SLineItems> _fkb2e400cb968f515 = createInvForeignKey(id, "order__id");

    public SOrder(String variable) {
        super(SOrder.class, forVariable(variable), "", "order_");
        addMetadata();
    }

    public SOrder(String variable, String schema, String table) {
        super(SOrder.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SOrder(Path<? extends SOrder> path) {
        super(path.getType(), path.getMetadata(), "", "order_");
        addMetadata();
    }

    public SOrder(PathMetadata<?> metadata) {
        super(SOrder.class, metadata, "", "order_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(customerId, ColumnMetadata.named("customer_id").withIndex(3).ofType(4).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(paid, ColumnMetadata.named("paid").withIndex(2).ofType(-7).notNull());
    }

}

