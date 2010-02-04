package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemUsagePrivileges is a Querydsl query type for QSystemUsagePrivileges
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_USAGE_PRIVILEGES")
public class QSystemUsagePrivileges extends PEntity<QSystemUsagePrivileges> {

    public final PString grantee = createString("GRANTEE");

    public final PString grantor = createString("GRANTOR");

    public final PString isGrantable = createString("IS_GRANTABLE");

    public final PString objectCatalog = createString("OBJECT_CATALOG");

    public final PString objectName = createString("OBJECT_NAME");

    public final PString objectSchema = createString("OBJECT_SCHEMA");

    public final PString objectType = createString("OBJECT_TYPE");

    public QSystemUsagePrivileges(String variable) {
        super(QSystemUsagePrivileges.class, forVariable(variable));
    }

    public QSystemUsagePrivileges(PEntity<? extends QSystemUsagePrivileges> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemUsagePrivileges(PathMetadata<?> metadata) {
        super(QSystemUsagePrivileges.class, metadata);
    }

}

