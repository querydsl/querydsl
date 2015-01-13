package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SItem_statuschange is a Querydsl querydsl type for SItem_statuschange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SItem_statuschange extends com.querydsl.sql.RelationalPathBase<SItem_statuschange> {

    private static final long serialVersionUID = 210676994;

    public static final SItem_statuschange item_statuschange_ = new SItem_statuschange("item__statuschange_");

    public final NumberPath<Long> item_id = createNumber("item_id", Long.class);

    public final NumberPath<Long> statusChangesId = createNumber("statusChangesId", Long.class);

    public final com.querydsl.sql.ForeignKey<SStatuschange> fkcb99fb2ab2bd098d = createForeignKey(statusChangesId, "id");

    public final com.querydsl.sql.ForeignKey<SItem> fkcb99fb2aedc50192 = createForeignKey(item_id, "id");

    public SItem_statuschange(String variable) {
        super(SItem_statuschange.class, forVariable(variable), "", "item__statuschange_");
        addMetadata();
    }

    public SItem_statuschange(String variable, String schema, String table) {
        super(SItem_statuschange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SItem_statuschange(Path<? extends SItem_statuschange> path) {
        super(path.getType(), path.getMetadata(), "", "item__statuschange_");
        addMetadata();
    }

    public SItem_statuschange(PathMetadata<?> metadata) {
        super(SItem_statuschange.class, metadata, "", "item__statuschange_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(item_id, ColumnMetadata.named("item__id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(statusChangesId, ColumnMetadata.named("statusChanges_id").withIndex(2).ofType(-5).withSize(19).notNull());
    }

}

