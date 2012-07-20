package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyssequences is a Querydsl query type for SSyssequences
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSyssequences extends com.mysema.query.sql.RelationalPathBase<SSyssequences> {

    private static final long serialVersionUID = -1065496554;

    public static final SSyssequences syssequences = new SSyssequences("syssequences");

    public SSyssequences(String variable) {
        super(SSyssequences.class, forVariable(variable), "sys", "syssequences");
    }

    public SSyssequences(Path<? extends SSyssequences> path) {
        super(path.getType(), path.getMetadata(), "sys", "syssequences");
    }

    public SSyssequences(PathMetadata<?> metadata) {
        super(SSyssequences.class, metadata, "sys", "syssequences");
    }

}

