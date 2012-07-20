package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSystableperms is a Querydsl query type for SSystableperms
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSystableperms extends com.mysema.query.sql.RelationalPathBase<SSystableperms> {

    private static final long serialVersionUID = 4684209;

    public static final SSystableperms systableperms = new SSystableperms("systableperms");

    public SSystableperms(String variable) {
        super(SSystableperms.class, forVariable(variable), "sys", "systableperms");
    }

    public SSystableperms(Path<? extends SSystableperms> path) {
        super(path.getType(), path.getMetadata(), "sys", "systableperms");
    }

    public SSystableperms(PathMetadata<?> metadata) {
        super(SSystableperms.class, metadata, "sys", "systableperms");
    }

}

