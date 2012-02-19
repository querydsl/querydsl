package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStatus is a Querydsl query type for SStatus
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SStatus extends com.mysema.query.sql.RelationalPathBase<SStatus> {

    private static final long serialVersionUID = 855650339;

    public static final SStatus status = new SStatus("STATUS_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SStatus> sql120219232329600 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem> _fk5fde7acd23307bc = createInvForeignKey(id, "STATUS_ID");

    public final com.mysema.query.sql.ForeignKey<SItem> _fk5fde7ac9ea26263 = createInvForeignKey(id, "CURRENTSTATUS_ID");

    public SStatus(String variable) {
        super(SStatus.class, forVariable(variable), "APP", "STATUS_");
    }

    public SStatus(Path<? extends SStatus> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "STATUS_");
    }

    public SStatus(PathMetadata<?> metadata) {
        super(SStatus.class, metadata, "APP", "STATUS_");
    }

}

