package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.StringPath;


/**
 * SSysroles is a Querydsl query type for SSysroles
 */
public class SSysroles extends com.mysema.query.sql.RelationalPathBase<SSysroles> {

    private static final long serialVersionUID = -1697898495;

    public static final SSysroles sysroles = new SSysroles("SYSROLES");

    public final StringPath grantee = createString("GRANTEE");

    public final StringPath grantor = createString("GRANTOR");

    public final StringPath isdef = createString("ISDEF");

    public final StringPath roleid = createString("ROLEID");

    public final StringPath uuid = createString("UUID");

    public final StringPath withadminoption = createString("WITHADMINOPTION");

    public SSysroles(String variable) {
        super(SSysroles.class, forVariable(variable), "SYS", "SYSROLES");
    }

    public SSysroles(Path<? extends SSysroles> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSROLES");
    }

    public SSysroles(PathMetadata<?> metadata) {
        super(SSysroles.class, metadata, "SYS", "SYSROLES");
    }

}

