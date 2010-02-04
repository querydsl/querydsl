package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTriggers is a Querydsl query type for QSystemTriggers
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TRIGGERS")
public class QSystemTriggers extends PEntity<QSystemTriggers> {

    public final PString actionType = createString("ACTION_TYPE");

    public final PString baseObjectType = createString("BASE_OBJECT_TYPE");

    public final PString columnName = createString("COLUMN_NAME");

    public final PString description = createString("DESCRIPTION");

    public final PString referencingNames = createString("REFERENCING_NAMES");

    public final PString status = createString("STATUS");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PString triggerBody = createString("TRIGGER_BODY");

    public final PString triggerCat = createString("TRIGGER_CAT");

    public final PString triggerName = createString("TRIGGER_NAME");

    public final PString triggerSchem = createString("TRIGGER_SCHEM");

    public final PString triggerType = createString("TRIGGER_TYPE");

    public final PString triggeringEvent = createString("TRIGGERING_EVENT");

    public final PString whenClause = createString("WHEN_CLAUSE");

    public QSystemTriggers(String variable) {
        super(QSystemTriggers.class, forVariable(variable));
    }

    public QSystemTriggers(PEntity<? extends QSystemTriggers> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTriggers(PathMetadata<?> metadata) {
        super(QSystemTriggers.class, metadata);
    }

}

