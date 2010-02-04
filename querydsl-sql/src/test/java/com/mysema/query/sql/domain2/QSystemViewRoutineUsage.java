package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemViewRoutineUsage is a Querydsl query type for QSystemViewRoutineUsage
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_VIEW_ROUTINE_USAGE")
public class QSystemViewRoutineUsage extends PEntity<QSystemViewRoutineUsage> {

    public final PString specificCatalog = createString("SPECIFIC_CATALOG");

    public final PString specificName = createString("SPECIFIC_NAME");

    public final PString specificSchema = createString("SPECIFIC_SCHEMA");

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchema = createString("TABLE_SCHEMA");

    public QSystemViewRoutineUsage(String variable) {
        super(QSystemViewRoutineUsage.class, forVariable(variable));
    }

    public QSystemViewRoutineUsage(PEntity<? extends QSystemViewRoutineUsage> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemViewRoutineUsage(PathMetadata<?> metadata) {
        super(QSystemViewRoutineUsage.class, metadata);
    }

}

