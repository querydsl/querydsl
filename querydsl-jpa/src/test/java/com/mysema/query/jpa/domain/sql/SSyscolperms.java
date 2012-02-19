package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyscolperms is a Querydsl query type for SSyscolperms
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSyscolperms extends com.mysema.query.sql.RelationalPathBase<SSyscolperms> {

    private static final long serialVersionUID = -626716097;

    public static final SSyscolperms syscolperms = new SSyscolperms("SYSCOLPERMS");

    public final StringPath colpermsid = createString("COLPERMSID");

    public final SimplePath<Object> columns = createSimple("COLUMNS", Object.class);

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath tableid = createString("TABLEID");

    public final StringPath type = createString("TYPE");

    public SSyscolperms(String variable) {
        super(SSyscolperms.class, forVariable(variable), "SYS", "SYSCOLPERMS");
    }

    public SSyscolperms(Path<? extends SSyscolperms> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSCOLPERMS");
    }

    public SSyscolperms(PathMetadata<?> metadata) {
        super(SSyscolperms.class, metadata, "SYS", "SYSCOLPERMS");
    }

}

