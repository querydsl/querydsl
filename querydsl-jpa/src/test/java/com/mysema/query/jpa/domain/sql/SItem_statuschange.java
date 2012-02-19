package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SItem_statuschange is a Querydsl query type for SItem_statuschange
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SItem_statuschange extends com.mysema.query.sql.RelationalPathBase<SItem_statuschange> {

    private static final long serialVersionUID = 6796029;

    public static final SItem_statuschange item_statuschange = new SItem_statuschange("ITEM__STATUSCHANGE_");

    public final NumberPath<Long> item_id = createNumber("ITEM__ID", Long.class);

    public final NumberPath<Long> statuschangesId = createNumber("STATUSCHANGES_ID", Long.class);

    public final com.mysema.query.sql.ForeignKey<SStatuschange> fkcb99fb2ab2bd098d = createForeignKey(statuschangesId, "ID");

    public final com.mysema.query.sql.ForeignKey<SItem> fkcb99fb2aedc50192 = createForeignKey(item_id, "ID");

    public SItem_statuschange(String variable) {
        super(SItem_statuschange.class, forVariable(variable), "APP", "ITEM__STATUSCHANGE_");
    }

    public SItem_statuschange(Path<? extends SItem_statuschange> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "ITEM__STATUSCHANGE_");
    }

    public SItem_statuschange(PathMetadata<?> metadata) {
        super(SItem_statuschange.class, metadata, "APP", "ITEM__STATUSCHANGE_");
    }

}

