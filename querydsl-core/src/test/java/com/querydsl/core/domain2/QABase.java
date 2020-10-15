package com.querydsl.core.domain2;


import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QABase is a Querydsl query type for ABase
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

    public QABase(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QABase(PathMetadata metadata, PathInits inits) {
        this(ABase.class, metadata, inits);
    }

    public QABase(Class<? extends ABase> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tenant = inits.isInitialized("tenant") ? new QTenantImpl(forProperty("tenant")) : null;
    }

}

