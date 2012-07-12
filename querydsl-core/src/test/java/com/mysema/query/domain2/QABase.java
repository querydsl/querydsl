package com.mysema.query.domain2;


import static com.mysema.query.types.PathMetadataFactory.*;
import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QABase is a Querydsl query type for ABase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QABase extends EntityPathBase<ABase> {

    private static final long serialVersionUID = -261663299;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QABase aBase = new QABase("aBase");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTenantImpl tenant;

    public QABase(String variable) {
        this(ABase.class, forVariable(variable), INITS);
    }

    public QABase(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QABase(PathMetadata<?> metadata, PathInits inits) {
        this(ABase.class, metadata, inits);
    }

    public QABase(Class<? extends ABase> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tenant = inits.isInitialized("tenant") ? new QTenantImpl(forProperty("tenant")) : null;
    }

}

