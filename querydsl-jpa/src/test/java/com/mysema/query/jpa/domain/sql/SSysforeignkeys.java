package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysforeignkeys is a Querydsl query type for SSysforeignkeys
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSysforeignkeys extends com.mysema.query.sql.RelationalPathBase<SSysforeignkeys> {

    private static final long serialVersionUID = 1912713996;

    public static final SSysforeignkeys sysforeignkeys = new SSysforeignkeys("SYSFOREIGNKEYS");

    public final StringPath conglomerateid = createString("CONGLOMERATEID");

    public final StringPath constraintid = createString("CONSTRAINTID");

    public final StringPath deleterule = createString("DELETERULE");

    public final StringPath keyconstraintid = createString("KEYCONSTRAINTID");

    public final StringPath updaterule = createString("UPDATERULE");

    public SSysforeignkeys(String variable) {
        super(SSysforeignkeys.class, forVariable(variable), "SYS", "SYSFOREIGNKEYS");
    }

    public SSysforeignkeys(Path<? extends SSysforeignkeys> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSFOREIGNKEYS");
    }

    public SSysforeignkeys(PathMetadata<?> metadata) {
        super(SSysforeignkeys.class, metadata, "SYS", "SYSFOREIGNKEYS");
    }

}

