package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SItem_statuschange is a Querydsl query type for SItem_statuschange
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SItem_statuschange extends com.mysema.query.sql.RelationalPathBase<SItem_statuschange> {

    private static final long serialVersionUID = 6796029;

    public static final SItem_statuschange item_statuschange = new SItem_statuschange("item__statuschange_");

    public final NumberPath<Long> item_id = createNumber("item__id", Long.class);

    public final NumberPath<Long> paymentID = createNumber("Payment_ID", Long.class);

    public final NumberPath<Long> statusChangesID = createNumber("statusChanges_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SItem_statuschange> primary = createPrimaryKey(paymentID, statusChangesID);

    public final com.mysema.query.sql.ForeignKey<SItem> item_statuschange_PaymentIDFK = createForeignKey(paymentID, "ID");

    public final com.mysema.query.sql.ForeignKey<SStatuschange> item_statuschange_statusChangesIDFK = createForeignKey(statusChangesID, "ID");

    public final com.mysema.query.sql.ForeignKey<SItem> fkcb99fb2aedc50192 = createForeignKey(item_id, "ID");

    public SItem_statuschange(String variable) {
        super(SItem_statuschange.class, forVariable(variable), "null", "item__statuschange_");
    }

    public SItem_statuschange(Path<? extends SItem_statuschange> path) {
        super(path.getType(), path.getMetadata(), "null", "item__statuschange_");
    }

    public SItem_statuschange(PathMetadata<?> metadata) {
        super(SItem_statuschange.class, metadata, "null", "item__statuschange_");
    }

}

