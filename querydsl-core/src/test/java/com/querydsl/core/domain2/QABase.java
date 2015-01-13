package com.querydsl.core.domain2;


import static com.querydsl.core.types.PathMetadataFactory.*;
import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;

import javax.annotation.Generated;


/**
 * QABase is a Querydsl querydsl type for ABase
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
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

