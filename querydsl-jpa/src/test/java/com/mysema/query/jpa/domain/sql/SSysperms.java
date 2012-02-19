package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysperms is a Querydsl query type for SSysperms
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSysperms extends com.mysema.query.sql.RelationalPathBase<SSysperms> {

    private static final long serialVersionUID = -1700037433;

    public static final SSysperms sysperms = new SSysperms("SYSPERMS");

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath isgrantable = createString("ISGRANTABLE");

    public final StringPath objectid = createString("OBJECTID");

    public final StringPath objecttype = createString("OBJECTTYPE");

    public final StringPath permission = createString("PERMISSION");

    public final StringPath uuid = createString("UUID");

    public SSysperms(String variable) {
        super(SSysperms.class, forVariable(variable), "SYS", "SYSPERMS");
    }

    public SSysperms(Path<? extends SSysperms> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSPERMS");
    }

    public SSysperms(PathMetadata<?> metadata) {
        super(SSysperms.class, metadata, "SYS", "SYSPERMS");
    }

}

