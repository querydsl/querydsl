package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SOrderDeliveredItemIndices is a Querydsl querydsl type for SOrderDeliveredItemIndices
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SOrderDeliveredItemIndices extends com.querydsl.sql.RelationalPathBase<SOrderDeliveredItemIndices> {

    private static final long serialVersionUID = 860822157;

    public static final SOrderDeliveredItemIndices OrderDeliveredItemIndices = new SOrderDeliveredItemIndices("Order_deliveredItemIndices");

    public final NumberPath<Integer> _index = createNumber("_index", Integer.class);

    public final NumberPath<Integer> deliveredItemIndices = createNumber("deliveredItemIndices", Integer.class);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SOrderDeliveredItemIndices> primary = createPrimaryKey(orderId, _index);

    public final com.querydsl.sql.ForeignKey<SOrder> fk30cbd6611a4d2378 = createForeignKey(orderId, "id");

    public SOrderDeliveredItemIndices(String variable) {
        super(SOrderDeliveredItemIndices.class, forVariable(variable), "", "Order_deliveredItemIndices");
        addMetadata();
    }

    public SOrderDeliveredItemIndices(String variable, String schema, String table) {
        super(SOrderDeliveredItemIndices.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SOrderDeliveredItemIndices(Path<? extends SOrderDeliveredItemIndices> path) {
        super(path.getType(), path.getMetadata(), "", "Order_deliveredItemIndices");
        addMetadata();
    }

    public SOrderDeliveredItemIndices(PathMetadata<?> metadata) {
        super(SOrderDeliveredItemIndices.class, metadata, "", "Order_deliveredItemIndices");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(_index, ColumnMetadata.named("_index").withIndex(3).ofType(4).withSize(10).notNull());
        addMetadata(deliveredItemIndices, ColumnMetadata.named("deliveredItemIndices").withIndex(2).ofType(4).withSize(10));
        addMetadata(orderId, ColumnMetadata.named("Order_id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

