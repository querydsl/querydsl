package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysstatements is a Querydsl query type for SSysstatements
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysstatements extends com.mysema.query.sql.RelationalPathBase<SSysstatements> {

    private static final long serialVersionUID = 1278778912;

    public static final SSysstatements sysstatements = new SSysstatements("sysstatements");

    public SSysstatements(String variable) {
        super(SSysstatements.class, forVariable(variable), "sys", "sysstatements");
    }

    public SSysstatements(Path<? extends SSysstatements> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysstatements");
    }

    public SSysstatements(PathMetadata<?> metadata) {
        super(SSysstatements.class, metadata, "sys", "sysstatements");
    }

}

