package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStatuschange is a Querydsl query type for SStatuschange
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SStatuschange extends com.mysema.query.sql.RelationalPathBase<SStatuschange> {

    private static final long serialVersionUID = 141385107;

    public static final SStatuschange statuschange = new SStatuschange("STATUSCHANGE_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("TIMESTAMP", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<SStatuschange> sql120219232329920 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem_statuschange> _fkcb99fb2ab2bd098d = createInvForeignKey(id, "STATUSCHANGES_ID");

    public SStatuschange(String variable) {
        super(SStatuschange.class, forVariable(variable), "APP", "STATUSCHANGE_");
    }

    public SStatuschange(Path<? extends SStatuschange> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "STATUSCHANGE_");
    }

    public SStatuschange(PathMetadata<?> metadata) {
        super(SStatuschange.class, metadata, "APP", "STATUSCHANGE_");
    }

}

