package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysperms is a Querydsl query type for SSysperms
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysperms extends com.mysema.query.sql.RelationalPathBase<SSysperms> {

    private static final long serialVersionUID = -1700037433;

    public static final SSysperms sysperms = new SSysperms("sysperms");

    public SSysperms(String variable) {
        super(SSysperms.class, forVariable(variable), "sys", "sysperms");
    }

    public SSysperms(Path<? extends SSysperms> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysperms");
    }

    public SSysperms(PathMetadata<?> metadata) {
        super(SSysperms.class, metadata, "sys", "sysperms");
    }

}

