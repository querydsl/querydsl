package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStatus is a Querydsl query type for SStatus
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStatus extends com.mysema.query.sql.RelationalPathBase<SStatus> {

    private static final long serialVersionUID = 855650339;

    public static final SStatus status = new SStatus("status_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SStatus> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem> _item_CURRENTSTATUSIDFK = createInvForeignKey(id, "CURRENTSTATUS_ID");

    public final com.mysema.query.sql.ForeignKey<SItem> _item_STATUSIDFK = createInvForeignKey(id, "STATUS_ID");

    public SStatus(String variable) {
        super(SStatus.class, forVariable(variable), "null", "status_");
    }

    public SStatus(Path<? extends SStatus> path) {
        super(path.getType(), path.getMetadata(), "null", "status_");
    }

    public SStatus(PathMetadata<?> metadata) {
        super(SStatus.class, metadata, "null", "status_");
    }

}

