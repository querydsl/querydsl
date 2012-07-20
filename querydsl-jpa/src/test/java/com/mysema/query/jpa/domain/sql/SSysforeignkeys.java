package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysforeignkeys is a Querydsl query type for SSysforeignkeys
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysforeignkeys extends com.mysema.query.sql.RelationalPathBase<SSysforeignkeys> {

    private static final long serialVersionUID = 1912713996;

    public static final SSysforeignkeys sysforeignkeys = new SSysforeignkeys("sysforeignkeys");

    public SSysforeignkeys(String variable) {
        super(SSysforeignkeys.class, forVariable(variable), "sys", "sysforeignkeys");
    }

    public SSysforeignkeys(Path<? extends SSysforeignkeys> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysforeignkeys");
    }

    public SSysforeignkeys(PathMetadata<?> metadata) {
        super(SSysforeignkeys.class, metadata, "sys", "sysforeignkeys");
    }

}

