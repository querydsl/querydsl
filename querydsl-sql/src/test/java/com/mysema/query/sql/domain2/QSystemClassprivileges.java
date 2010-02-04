package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemClassprivileges is a Querydsl query type for QSystemClassprivileges
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CLASSPRIVILEGES")
public class QSystemClassprivileges extends PEntity<QSystemClassprivileges> {

    public final PString classCat = createString("CLASS_CAT");

    public final PString className = createString("CLASS_NAME");

    public final PString classSchem = createString("CLASS_SCHEM");

    public final PString grantee = createString("GRANTEE");

    public final PString grantor = createString("GRANTOR");

    public final PString isGrantable = createString("IS_GRANTABLE");

    public final PString privilege = createString("PRIVILEGE");

    public QSystemClassprivileges(String variable) {
        super(QSystemClassprivileges.class, forVariable(variable));
    }

    public QSystemClassprivileges(PEntity<? extends QSystemClassprivileges> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemClassprivileges(PathMetadata<?> metadata) {
        super(QSystemClassprivileges.class, metadata);
    }

}

