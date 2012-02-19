package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSyschecks is a Querydsl query type for SSyschecks
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSyschecks extends com.mysema.query.sql.RelationalPathBase<SSyschecks> {

    private static final long serialVersionUID = -1531358297;

    public static final SSyschecks syschecks = new SSyschecks("SYSCHECKS");

    public final StringPath checkdefinition = createString("CHECKDEFINITION");

    public final StringPath constraintid = createString("CONSTRAINTID");

    public final SimplePath<Object> referencedcolumns = createSimple("REFERENCEDCOLUMNS", Object.class);

    public SSyschecks(String variable) {
        super(SSyschecks.class, forVariable(variable), "SYS", "SYSCHECKS");
    }

    public SSyschecks(Path<? extends SSyschecks> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSCHECKS");
    }

    public SSyschecks(PathMetadata<?> metadata) {
        super(SSyschecks.class, metadata, "SYS", "SYSCHECKS");
    }

}

