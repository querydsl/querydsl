package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTriggercolumns is a Querydsl query type for QSystemTriggercolumns
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TRIGGERCOLUMNS")
public class QSystemTriggercolumns extends PEntity<QSystemTriggercolumns> {

    public final PString columnList = createString("COLUMN_LIST");

    public final PString columnName = createString("COLUMN_NAME");

    public final PString columnUsage = createString("COLUMN_USAGE");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PString triggerCat = createString("TRIGGER_CAT");

    public final PString triggerName = createString("TRIGGER_NAME");

    public final PString triggerSchem = createString("TRIGGER_SCHEM");

    public QSystemTriggercolumns(String variable) {
        super(QSystemTriggercolumns.class, forVariable(variable));
    }

    public QSystemTriggercolumns(PEntity<? extends QSystemTriggercolumns> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTriggercolumns(PathMetadata<?> metadata) {
        super(QSystemTriggercolumns.class, metadata);
    }

}

