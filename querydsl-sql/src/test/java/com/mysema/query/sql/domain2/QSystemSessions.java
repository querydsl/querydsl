package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemSessions is a Querydsl query type for QSystemSessions
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_SESSIONS")
public class QSystemSessions extends PEntity<QSystemSessions> {

    public final PBoolean autocommit = createBoolean("AUTOCOMMIT");

    public final PComparable<java.util.Date> connected = createComparable("CONNECTED", java.util.Date.class);

    public final PBoolean isAdmin = createBoolean("IS_ADMIN");

    public final PNumber<Long> lastIdentity = createNumber("LAST_IDENTITY", Long.class);

    public final PNumber<Integer> maxrows = createNumber("MAXROWS", Integer.class);

    public final PBoolean readonly = createBoolean("READONLY");

    public final PString schema = createString("SCHEMA");

    public final PNumber<Integer> sessionId = createNumber("SESSION_ID", Integer.class);

    public final PNumber<Integer> transactionSize = createNumber("TRANSACTION_SIZE", Integer.class);

    public final PString userName = createString("USER_NAME");

    public QSystemSessions(String variable) {
        super(QSystemSessions.class, forVariable(variable));
    }

    public QSystemSessions(PEntity<? extends QSystemSessions> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemSessions(PathMetadata<?> metadata) {
        super(QSystemSessions.class, metadata);
    }

}

