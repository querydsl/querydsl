package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysconglomerates is a Querydsl query type for SSysconglomerates
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysconglomerates extends com.mysema.query.sql.RelationalPathBase<SSysconglomerates> {

    private static final long serialVersionUID = 2134368727;

    public static final SSysconglomerates sysconglomerates = new SSysconglomerates("sysconglomerates");

    public SSysconglomerates(String variable) {
        super(SSysconglomerates.class, forVariable(variable), "sys", "sysconglomerates");
    }

    public SSysconglomerates(Path<? extends SSysconglomerates> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysconglomerates");
    }

    public SSysconglomerates(PathMetadata<?> metadata) {
        super(SSysconglomerates.class, metadata, "sys", "sysconglomerates");
    }

}

