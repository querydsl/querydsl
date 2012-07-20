package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysdummy1 is a Querydsl query type for SSysdummy1
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSysdummy1 extends com.mysema.query.sql.RelationalPathBase<SSysdummy1> {

    private static final long serialVersionUID = -1490475067;

    public static final SSysdummy1 sysdummy1 = new SSysdummy1("sysdummy1");

    public SSysdummy1(String variable) {
        super(SSysdummy1.class, forVariable(variable), "sysibm", "sysdummy1");
    }

    public SSysdummy1(Path<? extends SSysdummy1> path) {
        super(path.getType(), path.getMetadata(), "sysibm", "sysdummy1");
    }

    public SSysdummy1(PathMetadata<?> metadata) {
        super(SSysdummy1.class, metadata, "sysibm", "sysdummy1");
    }

}

