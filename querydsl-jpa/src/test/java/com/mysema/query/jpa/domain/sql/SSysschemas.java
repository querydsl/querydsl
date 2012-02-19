package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysschemas is a Querydsl query type for SSysschemas
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSysschemas extends com.mysema.query.sql.RelationalPathBase<SSysschemas> {

    private static final long serialVersionUID = 947375926;

    public static final SSysschemas sysschemas = new SSysschemas("SYSSCHEMAS");

    public final StringPath authorizationid = createString("AUTHORIZATIONID");

    public final StringPath schemaid = createString("SCHEMAID");

    public final StringPath schemaname = createString("SCHEMANAME");

    public SSysschemas(String variable) {
        super(SSysschemas.class, forVariable(variable), "SYS", "SYSSCHEMAS");
    }

    public SSysschemas(Path<? extends SSysschemas> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSSCHEMAS");
    }

    public SSysschemas(PathMetadata<?> metadata) {
        super(SSysschemas.class, metadata, "SYS", "SYSSCHEMAS");
    }

}

