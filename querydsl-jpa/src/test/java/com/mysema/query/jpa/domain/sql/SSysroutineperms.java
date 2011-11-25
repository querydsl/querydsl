package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;


/**
 * SSysroutineperms is a Querydsl query type for SSysroutineperms
 */
public class SSysroutineperms extends com.mysema.query.sql.RelationalPathBase<SSysroutineperms> {

    private static final long serialVersionUID = 446537595;

    public static final SSysroutineperms sysroutineperms = new SSysroutineperms("SYSROUTINEPERMS");

    public final StringPath aliasid = createString("ALIASID");

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantoption = createString("GRANTOPTION");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath routinepermsid = createString("ROUTINEPERMSID");

    public SSysroutineperms(String variable) {
        super(SSysroutineperms.class, forVariable(variable), "SYS", "SYSROUTINEPERMS");
    }

    public SSysroutineperms(Path<? extends SSysroutineperms> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSROUTINEPERMS");
    }

    public SSysroutineperms(PathMetadata<?> metadata) {
        super(SSysroutineperms.class, metadata, "SYS", "SYSROUTINEPERMS");
    }

}

