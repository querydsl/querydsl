package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SItem is a Querydsl querydsl type for SItem
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SItem extends com.querydsl.sql.RelationalPathBase<SItem> {

    private static final long serialVersionUID = -120177317;

    public static final SItem item_ = new SItem("item_");

    public final NumberPath<Long> currentStatusId = createNumber("currentStatusId", Long.class);

    public final StringPath dtype = createString("dtype");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> paymentStatus = createNumber("paymentStatus", Integer.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Long> statusId = createNumber("statusId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SItem> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SStatus> fk5fde7acd23307bc = createForeignKey(statusId, "id");

    public final com.querydsl.sql.ForeignKey<SStatus> fk5fde7ac9ea26263 = createForeignKey(currentStatusId, "id");

    public final com.querydsl.sql.ForeignKey<SItem> fk5fde7ac2c7f0c58 = createForeignKey(productId, "id");

    public final com.querydsl.sql.ForeignKey<SAuditlog> _fkb88fbf6ae26109c = createInvForeignKey(id, "item_id");

    public final com.querydsl.sql.ForeignKey<SItem> _fk5fde7ac2c7f0c58 = createInvForeignKey(id, "product_id");

    public final com.querydsl.sql.ForeignKey<SOrder_item> _fk1b5e8cbe7640c8cf = createInvForeignKey(id, "items_id");

    public final com.querydsl.sql.ForeignKey<SPrice> _fkc59678362c7f0c58 = createInvForeignKey(id, "product_id");

    public final com.querydsl.sql.ForeignKey<SItem_statuschange> _fkcb99fb2aedc50192 = createInvForeignKey(id, "item__id");

    public final com.querydsl.sql.ForeignKey<SLineItems> _fkb2e400c3d8e44c3 = createInvForeignKey(id, "lineItems_id");

    public SItem(String variable) {
        super(SItem.class, forVariable(variable), "", "item_");
        addMetadata();
    }

    public SItem(String variable, String schema, String table) {
        super(SItem.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SItem(Path<? extends SItem> path) {
        super(path.getType(), path.getMetadata(), "", "item_");
        addMetadata();
    }

    public SItem(PathMetadata<?> metadata) {
        super(SItem.class, metadata, "", "item_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(currentStatusId, ColumnMetadata.named("currentStatus_id").withIndex(6).ofType(-5).withSize(19));
        addMetadata(dtype, ColumnMetadata.named("DTYPE").withIndex(1).ofType(12).withSize(31).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(4).ofType(12).withSize(255));
        addMetadata(paymentStatus, ColumnMetadata.named("paymentStatus").withIndex(3).ofType(4).withSize(10));
        addMetadata(productId, ColumnMetadata.named("product_id").withIndex(5).ofType(-5).withSize(19));
        addMetadata(statusId, ColumnMetadata.named("status_id").withIndex(7).ofType(-5).withSize(19));
    }

}

