package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemColumnprivileges is a Querydsl query type for QSystemColumnprivileges
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_COLUMNPRIVILEGES")
public class QSystemColumnprivileges extends PEntity<QSystemColumnprivileges> {

    public final PString columnName = createString("COLUMN_NAME");

    public final PString grantee = createString("GRANTEE");

    public final PString grantor = createString("GRANTOR");

    public final PString isGrantable = createString("IS_GRANTABLE");

    public final PString privilege = createString("PRIVILEGE");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public QSystemColumnprivileges(String variable) {
        super(QSystemColumnprivileges.class, forVariable(variable));
    }

    public QSystemColumnprivileges(PEntity<? extends QSystemColumnprivileges> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemColumnprivileges(PathMetadata<?> metadata) {
        super(QSystemColumnprivileges.class, metadata);
    }

}

