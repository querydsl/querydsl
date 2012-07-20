package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysschemas is a Querydsl query type for SSysschemas
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysschemas extends com.mysema.query.sql.RelationalPathBase<SSysschemas> {

    private static final long serialVersionUID = 947375926;

    public static final SSysschemas sysschemas = new SSysschemas("sysschemas");

    public SSysschemas(String variable) {
        super(SSysschemas.class, forVariable(variable), "sys", "sysschemas");
    }

    public SSysschemas(Path<? extends SSysschemas> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysschemas");
    }

    public SSysschemas(PathMetadata<?> metadata) {
        super(SSysschemas.class, metadata, "sys", "sysschemas");
    }

}

