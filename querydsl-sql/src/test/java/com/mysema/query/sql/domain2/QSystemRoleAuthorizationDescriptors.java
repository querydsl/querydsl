package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemRoleAuthorizationDescriptors is a Querydsl query type for QSystemRoleAuthorizationDescriptors
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_ROLE_AUTHORIZATION_DESCRIPTORS")
public class QSystemRoleAuthorizationDescriptors extends PEntity<QSystemRoleAuthorizationDescriptors> {

    public final PString grantee = createString("GRANTEE");

    public final PString grantor = createString("GRANTOR");

    public final PString isGrantable = createString("IS_GRANTABLE");

    public final PString roleName = createString("ROLE_NAME");

    public QSystemRoleAuthorizationDescriptors(String variable) {
        super(QSystemRoleAuthorizationDescriptors.class, forVariable(variable));
    }

    public QSystemRoleAuthorizationDescriptors(PEntity<? extends QSystemRoleAuthorizationDescriptors> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemRoleAuthorizationDescriptors(PathMetadata<?> metadata) {
        super(QSystemRoleAuthorizationDescriptors.class, metadata);
    }

}

