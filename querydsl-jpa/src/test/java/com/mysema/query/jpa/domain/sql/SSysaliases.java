package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysaliases is a Querydsl query type for SSysaliases
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysaliases extends com.mysema.query.sql.RelationalPathBase<SSysaliases> {

    private static final long serialVersionUID = -1884315838;

    public static final SSysaliases sysaliases = new SSysaliases("sysaliases");

    public SSysaliases(String variable) {
        super(SSysaliases.class, forVariable(variable), "sys", "sysaliases");
    }

    public SSysaliases(Path<? extends SSysaliases> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysaliases");
    }

    public SSysaliases(PathMetadata<?> metadata) {
        super(SSysaliases.class, metadata, "sys", "sysaliases");
    }

}

