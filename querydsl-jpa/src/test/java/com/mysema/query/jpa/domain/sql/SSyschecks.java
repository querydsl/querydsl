package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyschecks is a Querydsl query type for SSyschecks
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSyschecks extends com.mysema.query.sql.RelationalPathBase<SSyschecks> {

    private static final long serialVersionUID = -1531358297;

    public static final SSyschecks syschecks = new SSyschecks("syschecks");

    public SSyschecks(String variable) {
        super(SSyschecks.class, forVariable(variable), "sys", "syschecks");
    }

    public SSyschecks(Path<? extends SSyschecks> path) {
        super(path.getType(), path.getMetadata(), "sys", "syschecks");
    }

    public SSyschecks(PathMetadata<?> metadata) {
        super(SSyschecks.class, metadata, "sys", "syschecks");
    }

}

