package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysviews is a Querydsl query type for SSysviews
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysviews extends com.mysema.query.sql.RelationalPathBase<SSysviews> {

    private static final long serialVersionUID = -1694389326;

    public static final SSysviews sysviews = new SSysviews("sysviews");

    public SSysviews(String variable) {
        super(SSysviews.class, forVariable(variable), "sys", "sysviews");
    }

    public SSysviews(Path<? extends SSysviews> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysviews");
    }

    public SSysviews(PathMetadata<?> metadata) {
        super(SSysviews.class, metadata, "sys", "sysviews");
    }

}

