package com.mysema.query.domain2;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QTenantImpl is a Querydsl query type for TenantImpl
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
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

