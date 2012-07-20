package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysfiles is a Querydsl query type for SSysfiles
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysfiles extends com.mysema.query.sql.RelationalPathBase<SSysfiles> {

    private static final long serialVersionUID = -1709159493;

    public static final SSysfiles sysfiles = new SSysfiles("sysfiles");

    public SSysfiles(String variable) {
        super(SSysfiles.class, forVariable(variable), "sys", "sysfiles");
    }

    public SSysfiles(Path<? extends SSysfiles> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysfiles");
    }

    public SSysfiles(PathMetadata<?> metadata) {
        super(SSysfiles.class, metadata, "sys", "sysfiles");
    }

}

