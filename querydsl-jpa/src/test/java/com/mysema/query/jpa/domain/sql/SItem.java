package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SItem is a Querydsl query type for SItem
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SItem extends com.mysema.query.sql.RelationalPathBase<SItem> {

    private static final long serialVersionUID = -1389350012;

    public static final SItem item = new SItem("item_");

    public final NumberPath<Long> currentstatusId = createNumber("CURRENTSTATUS_ID", Long.class);

    public final StringPath dtype = createString("DTYPE");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> name = createNumber("NAME", Integer.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final NumberPath<Long> statusId = createNumber("STATUS_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SItem> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SStatus> item_CURRENTSTATUSIDFK = createForeignKey(currentstatusId, "ID");

    public final com.mysema.query.sql.ForeignKey<SItem> item_PRODUCTIDFK = createForeignKey(productId, "ID");

    public final com.mysema.query.sql.ForeignKey<SStatus> item_STATUSIDFK = createForeignKey(statusId, "ID");

    public final com.mysema.query.sql.ForeignKey<SItem> _item_PRODUCTIDFK = createInvForeignKey(id, "PRODUCT_ID");

    public final com.mysema.query.sql.ForeignKey<SOrder_item> _order_item_lineItemsIDFK = createInvForeignKey(id, "lineItems_ID");

    public final com.mysema.query.sql.ForeignKey<SItem_statuschange> _item_statuschange_PaymentIDFK = createInvForeignKey(id, "Payment_ID");

    public final com.mysema.query.sql.ForeignKey<SAuditlog> _auditlog_ITEMIDFK = createInvForeignKey(id, "ITEM_ID");

    public final com.mysema.query.sql.ForeignKey<SOrder_item> _order_item_itemsIDFK = createInvForeignKey(id, "items_ID");

    public final com.mysema.query.sql.ForeignKey<SPrice> _price_PRODUCTIDFK = createInvForeignKey(id, "PRODUCT_ID");

    public final com.mysema.query.sql.ForeignKey<SItem_statuschange> _fkcb99fb2aedc50192 = createInvForeignKey(id, "item__id");

    public SItem(String variable) {
        super(SItem.class, forVariable(variable), "null", "item_");
    }

    public SItem(Path<? extends SItem> path) {
        super(path.getType(), path.getMetadata(), "null", "item_");
    }

    public SItem(PathMetadata<?> metadata) {
        super(SItem.class, metadata, "null", "item_");
    }

}

