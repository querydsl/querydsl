package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SOrder_item is a Querydsl query type for SOrder_item
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SOrder_item extends com.mysema.query.sql.RelationalPathBase<SOrder_item> {

    private static final long serialVersionUID = 2009459989;

    public static final SOrder_item order_item = new SOrder_item("order__item_");

    public final NumberPath<Integer> _index = createNumber("_index", Integer.class);

    public final NumberPath<Long> itemsID = createNumber("items_ID", Long.class);

    public final NumberPath<Long> lineItemsID = createNumber("lineItems_ID", Long.class);

    public final NumberPath<Long> order_id = createNumber("order__id", Long.class);

    public final NumberPath<Long> orderID = createNumber("Order_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SOrder_item> primary = createPrimaryKey(orderID, itemsID, lineItemsID);

    public final com.mysema.query.sql.ForeignKey<SOrder> order_item_OrderIDFK = createForeignKey(orderID, "id");

    public final com.mysema.query.sql.ForeignKey<SItem> order_item_lineItemsIDFK = createForeignKey(lineItemsID, "ID");

    public final com.mysema.query.sql.ForeignKey<SItem> order_item_itemsIDFK = createForeignKey(itemsID, "ID");

    public final com.mysema.query.sql.ForeignKey<SOrder> fk1b5e8cbeb968f515 = createForeignKey(order_id, "id");

    public SOrder_item(String variable) {
        super(SOrder_item.class, forVariable(variable), "null", "order__item_");
    }

    public SOrder_item(Path<? extends SOrder_item> path) {
        super(path.getType(), path.getMetadata(), "null", "order__item_");
    }

    public SOrder_item(PathMetadata<?> metadata) {
        super(SOrder_item.class, metadata, "null", "order__item_");
    }

}

