package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysstatistics is a Querydsl query type for SSysstatistics
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysstatistics extends com.mysema.query.sql.RelationalPathBase<SSysstatistics> {

    private static final long serialVersionUID = 1399278175;

    public static final SSysstatistics sysstatistics = new SSysstatistics("sysstatistics");

    public SSysstatistics(String variable) {
        super(SSysstatistics.class, forVariable(variable), "sys", "sysstatistics");
    }

    public SSysstatistics(Path<? extends SSysstatistics> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysstatistics");
    }

    public SSysstatistics(PathMetadata<?> metadata) {
        super(SSysstatistics.class, metadata, "sys", "sysstatistics");
    }

}

