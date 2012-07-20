package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSystriggers is a Querydsl query type for SSystriggers
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSystriggers extends com.mysema.query.sql.RelationalPathBase<SSystriggers> {

    private static final long serialVersionUID = 1504647671;

    public static final SSystriggers systriggers = new SSystriggers("systriggers");

    public SSystriggers(String variable) {
        super(SSystriggers.class, forVariable(variable), "sys", "systriggers");
    }

    public SSystriggers(Path<? extends SSystriggers> path) {
        super(path.getType(), path.getMetadata(), "sys", "systriggers");
    }

    public SSystriggers(PathMetadata<?> metadata) {
        super(SSystriggers.class, metadata, "sys", "systriggers");
    }

}

