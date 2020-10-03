package com.querydsl.core.domain2;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;


/**
 * QTenantImpl is a Querydsl query type for TenantImpl
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

    public QTenantImpl(PathMetadata metadata) {
        super(TenantImpl.class, metadata);
    }

}

