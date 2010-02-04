package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemUsers is a Querydsl query type for QSystemUsers
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_USERS")
public class QSystemUsers extends PEntity<QSystemUsers> {

    public final PBoolean admin = createBoolean("ADMIN");

    public final PString user = createString("USER");

    public QSystemUsers(String variable) {
        super(QSystemUsers.class, forVariable(variable));
    }

    public QSystemUsers(PEntity<? extends QSystemUsers> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemUsers(PathMetadata<?> metadata) {
        super(QSystemUsers.class, metadata);
    }

}

