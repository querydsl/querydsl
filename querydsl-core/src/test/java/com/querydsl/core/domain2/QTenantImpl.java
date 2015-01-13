package com.querydsl.core.domain2;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;

import javax.annotation.Generated;


/**
 * QTenantImpl is a Querydsl querydsl type for TenantImpl
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTenantImpl extends EntityPathBase<TenantImpl> {

    private static final long serialVersionUID = -1856470561;

    public static final QTenantImpl tenantImpl = new QTenantImpl("tenantImpl");

    public QTenantImpl(String variable) {
        super(TenantImpl.class, forVariable(variable));
    }

    public QTenantImpl(Path<? extends TenantImpl> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTenantImpl(PathMetadata<?> metadata) {
        super(TenantImpl.class, metadata);
    }

}

