package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysroutineperms is a Querydsl query type for SSysroutineperms
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysroutineperms extends com.mysema.query.sql.RelationalPathBase<SSysroutineperms> {

    private static final long serialVersionUID = 446537595;

    public static final SSysroutineperms sysroutineperms = new SSysroutineperms("sysroutineperms");

    public SSysroutineperms(String variable) {
        super(SSysroutineperms.class, forVariable(variable), "sys", "sysroutineperms");
    }

    public SSysroutineperms(Path<? extends SSysroutineperms> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysroutineperms");
    }

    public SSysroutineperms(PathMetadata<?> metadata) {
        super(SSysroutineperms.class, metadata, "sys", "sysroutineperms");
    }

}

