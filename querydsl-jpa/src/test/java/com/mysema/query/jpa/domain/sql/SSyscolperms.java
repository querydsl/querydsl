package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyscolperms is a Querydsl query type for SSyscolperms
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSyscolperms extends com.mysema.query.sql.RelationalPathBase<SSyscolperms> {

    private static final long serialVersionUID = -626716097;

    public static final SSyscolperms syscolperms = new SSyscolperms("syscolperms");

    public SSyscolperms(String variable) {
        super(SSyscolperms.class, forVariable(variable), "sys", "syscolperms");
    }

    public SSyscolperms(Path<? extends SSyscolperms> path) {
        super(path.getType(), path.getMetadata(), "sys", "syscolperms");
    }

    public SSyscolperms(PathMetadata<?> metadata) {
        super(SSyscolperms.class, metadata, "sys", "syscolperms");
    }

}

