package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;


/**
 * SSysdummy1 is a Querydsl query type for SSysdummy1
 */
public class SSysdummy1 extends com.mysema.query.sql.RelationalPathBase<SSysdummy1> {

    private static final long serialVersionUID = -1490475067;

    public static final SSysdummy1 sysdummy1 = new SSysdummy1("SYSDUMMY1");

    public final StringPath ibmreqd = createString("IBMREQD");

    public SSysdummy1(String variable) {
        super(SSysdummy1.class, forVariable(variable), "SYSIBM", "SYSDUMMY1");
    }

    public SSysdummy1(Path<? extends SSysdummy1> entity) {
        super(entity.getType(), entity.getMetadata(), "SYSIBM", "SYSDUMMY1");
    }

    public SSysdummy1(PathMetadata<?> metadata) {
        super(SSysdummy1.class, metadata, "SYSIBM", "SYSDUMMY1");
    }

}

