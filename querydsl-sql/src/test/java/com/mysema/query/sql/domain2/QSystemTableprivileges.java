package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTableprivileges is a Querydsl query type for QSystemTableprivileges
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TABLEPRIVILEGES")
public class QSystemTableprivileges extends PEntity<QSystemTableprivileges> {

    public final PString grantee = createString("GRANTEE");

    public final PString grantor = createString("GRANTOR");

    public final PString isGrantable = createString("IS_GRANTABLE");

    public final PString privilege = createString("PRIVILEGE");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public QSystemTableprivileges(String variable) {
        super(QSystemTableprivileges.class, forVariable(variable));
    }

    public QSystemTableprivileges(PEntity<? extends QSystemTableprivileges> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTableprivileges(PathMetadata<?> metadata) {
        super(QSystemTableprivileges.class, metadata);
    }

}

