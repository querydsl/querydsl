package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SOrderDeliveredItemIndices is a Querydsl query type for SOrderDeliveredItemIndices
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SOrderDeliveredItemIndices extends com.mysema.query.sql.RelationalPathBase<SOrderDeliveredItemIndices> {

    private static final long serialVersionUID = 860822157;

    public static final SOrderDeliveredItemIndices OrderDeliveredItemIndices = new SOrderDeliveredItemIndices("Order_deliveredItemIndices");

    public final NumberPath<Integer> _index = createNumber("_index", Integer.class);

    public final NumberPath<Integer> deliveredItemIndices = createNumber("deliveredItemIndices", Integer.class);

    public final NumberPath<Long> orderId = createNumber("Order_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SOrderDeliveredItemIndices> primary = createPrimaryKey(orderId, _index);

    public final com.mysema.query.sql.ForeignKey<SOrder> fk30cbd6611a4d2378 = createForeignKey(orderId, "id");

    public SOrderDeliveredItemIndices(String variable) {
        super(SOrderDeliveredItemIndices.class, forVariable(variable), "null", "Order_deliveredItemIndices");
    }

    public SOrderDeliveredItemIndices(Path<? extends SOrderDeliveredItemIndices> path) {
        super(path.getType(), path.getMetadata(), "null", "Order_deliveredItemIndices");
    }

    public SOrderDeliveredItemIndices(PathMetadata<?> metadata) {
        super(SOrderDeliveredItemIndices.class, metadata, "null", "Order_deliveredItemIndices");
    }

}

