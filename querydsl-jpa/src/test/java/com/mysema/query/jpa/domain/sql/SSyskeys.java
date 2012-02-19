package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyskeys is a Querydsl query type for SSyskeys
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSyskeys extends com.mysema.query.sql.RelationalPathBase<SSyskeys> {

    private static final long serialVersionUID = 914842672;

    public static final SSyskeys syskeys = new SSyskeys("SYSKEYS");

    public final StringPath conglomerateid = createString("CONGLOMERATEID");

    public final StringPath constraintid = createString("CONSTRAINTID");

    public SSyskeys(String variable) {
        super(SSyskeys.class, forVariable(variable), "SYS", "SYSKEYS");
    }

    public SSyskeys(Path<? extends SSyskeys> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSKEYS");
    }

    public SSyskeys(PathMetadata<?> metadata) {
        super(SSyskeys.class, metadata, "SYS", "SYSKEYS");
    }

}

