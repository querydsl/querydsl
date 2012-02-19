package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSysconglomerates is a Querydsl query type for SSysconglomerates
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SSysconglomerates extends com.mysema.query.sql.RelationalPathBase<SSysconglomerates> {

    private static final long serialVersionUID = 2134368727;

    public static final SSysconglomerates sysconglomerates = new SSysconglomerates("SYSCONGLOMERATES");

    public final StringPath conglomerateid = createString("CONGLOMERATEID");

    public final StringPath conglomeratename = createString("CONGLOMERATENAME");

    public final NumberPath<Long> conglomeratenumber = createNumber("CONGLOMERATENUMBER", Long.class);

    public final SimplePath<Object> descriptor = createSimple("DESCRIPTOR", Object.class);

    public final BooleanPath isconstraint = createBoolean("ISCONSTRAINT");

    public final BooleanPath isindex = createBoolean("ISINDEX");

    public final StringPath schemaid = createString("SCHEMAID");

    public final StringPath tableid = createString("TABLEID");

    public SSysconglomerates(String variable) {
        super(SSysconglomerates.class, forVariable(variable), "SYS", "SYSCONGLOMERATES");
    }

    public SSysconglomerates(Path<? extends SSysconglomerates> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSCONGLOMERATES");
    }

    public SSysconglomerates(PathMetadata<?> metadata) {
        super(SSysconglomerates.class, metadata, "SYS", "SYSCONGLOMERATES");
    }

}

