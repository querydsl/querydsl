package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysroles is a Querydsl query type for SSysroles
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysroles extends com.mysema.query.sql.RelationalPathBase<SSysroles> {

    private static final long serialVersionUID = -1697898495;

    public static final SSysroles sysroles = new SSysroles("sysroles");

    public SSysroles(String variable) {
        super(SSysroles.class, forVariable(variable), "sys", "sysroles");
    }

    public SSysroles(Path<? extends SSysroles> path) {
        super(path.getType(), path.getMetadata(), "sys", "sysroles");
    }

    public SSysroles(PathMetadata<?> metadata) {
        super(SSysroles.class, metadata, "sys", "sysroles");
    }

}

