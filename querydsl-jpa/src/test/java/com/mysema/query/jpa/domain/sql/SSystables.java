package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSystables is a Querydsl query type for SSystables
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSystables extends com.mysema.query.sql.RelationalPathBase<SSystables> {

    private static final long serialVersionUID = -1051208287;

    public static final SSystables systables = new SSystables("systables");

    public SSystables(String variable) {
        super(SSystables.class, forVariable(variable), "sys", "systables");
    }

    public SSystables(Path<? extends SSystables> path) {
        super(path.getType(), path.getMetadata(), "sys", "systables");
    }

    public SSystables(PathMetadata<?> metadata) {
        super(SSystables.class, metadata, "sys", "systables");
    }

}

