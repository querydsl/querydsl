package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStatuschange is a Querydsl query type for SStatuschange
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStatuschange extends com.mysema.query.sql.RelationalPathBase<SStatuschange> {

    private static final long serialVersionUID = 141385107;

    public static final SStatuschange statuschange = new SStatuschange("statuschange_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("TIMESTAMP", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<SStatuschange> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem_statuschange> _item_statuschange_statusChangesIDFK = createInvForeignKey(id, "statusChanges_ID");

    public SStatuschange(String variable) {
        super(SStatuschange.class, forVariable(variable), "null", "statuschange_");
    }

    public SStatuschange(Path<? extends SStatuschange> path) {
        super(path.getType(), path.getMetadata(), "null", "statuschange_");
    }

    public SStatuschange(PathMetadata<?> metadata) {
        super(SStatuschange.class, metadata, "null", "statuschange_");
    }

}

