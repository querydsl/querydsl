package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyskeys is a Querydsl query type for SSyskeys
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSyskeys extends com.mysema.query.sql.RelationalPathBase<SSyskeys> {

    private static final long serialVersionUID = 914842672;

    public static final SSyskeys syskeys = new SSyskeys("syskeys");

    public SSyskeys(String variable) {
        super(SSyskeys.class, forVariable(variable), "sys", "syskeys");
    }

    public SSyskeys(Path<? extends SSyskeys> path) {
        super(path.getType(), path.getMetadata(), "sys", "syskeys");
    }

    public SSyskeys(PathMetadata<?> metadata) {
        super(SSyskeys.class, metadata, "sys", "syskeys");
    }

}

