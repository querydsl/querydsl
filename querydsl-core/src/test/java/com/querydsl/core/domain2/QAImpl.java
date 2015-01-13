package com.querydsl.core.domain2;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;

import javax.annotation.Generated;


/**
 * QAImpl is a Querydsl querydsl type for AImpl
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAImpl extends EntityPathBase<AImpl> {

    private static final long serialVersionUID = -261443316;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QAImpl aImpl = new QAImpl("aImpl");

    public final QABase _super;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QTenantImpl tenant;

    public QAImpl(String variable) {
        this(AImpl.class, forVariable(variable), INITS);
    }

    public QAImpl(Path<? extends AImpl> entity) {
        this(entity.getType(), entity.getMetadata(), INITS);
    }
    
    public QAImpl(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAImpl(PathMetadata<?> metadata, PathInits inits) {
        this(AImpl.class, metadata, inits);
    }

    public QAImpl(Class<? extends AImpl> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QABase(type, metadata, inits);
        this.id = _super.id;
        this.tenant = _super.tenant;
    }

}

