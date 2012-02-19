package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SItem is a Querydsl query type for SItem
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SItem extends com.mysema.query.sql.RelationalPathBase<SItem> {

    private static final long serialVersionUID = -1389350012;

    public static final SItem item = new SItem("ITEM_");

    public final NumberPath<Long> currentstatusId = createNumber("CURRENTSTATUS_ID", Long.class);

    public final StringPath dtype = createString("DTYPE");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> name = createNumber("NAME", Integer.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final NumberPath<Long> statusId = createNumber("STATUS_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SItem> sql120219232325050 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SStatus> fk5fde7acd23307bc = createForeignKey(statusId, "ID");

    public final com.mysema.query.sql.ForeignKey<SStatus> fk5fde7ac9ea26263 = createForeignKey(currentstatusId, "ID");

    public final com.mysema.query.sql.ForeignKey<SItem> fk5fde7ac2c7f0c58 = createForeignKey(productId, "ID");

    public final com.mysema.query.sql.ForeignKey<SAuditlog> _fkb88fbf6ae26109c = createInvForeignKey(id, "ITEM_ID");

    public final com.mysema.query.sql.ForeignKey<SItem> _fk5fde7ac2c7f0c58 = createInvForeignKey(id, "PRODUCT_ID");

    public final com.mysema.query.sql.ForeignKey<SPrice> _fkc59678362c7f0c58 = createInvForeignKey(id, "PRODUCT_ID");

    public final com.mysema.query.sql.ForeignKey<SItem_statuschange> _fkcb99fb2aedc50192 = createInvForeignKey(id, "ITEM__ID");

    public SItem(String variable) {
        super(SItem.class, forVariable(variable), "APP", "ITEM_");
    }

    public SItem(Path<? extends SItem> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "ITEM_");
    }

    public SItem(PathMetadata<?> metadata) {
        super(SItem.class, metadata, "APP", "ITEM_");
    }

}

