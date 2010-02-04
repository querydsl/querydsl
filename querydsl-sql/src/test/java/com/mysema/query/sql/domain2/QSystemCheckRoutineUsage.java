package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCheckRoutineUsage is a Querydsl query type for QSystemCheckRoutineUsage
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CHECK_ROUTINE_USAGE")
public class QSystemCheckRoutineUsage extends PEntity<QSystemCheckRoutineUsage> {

    public final PString constraintCatalog = createString("CONSTRAINT_CATALOG");

    public final PString constraintName = createString("CONSTRAINT_NAME");

    public final PString constraintSchema = createString("CONSTRAINT_SCHEMA");

    public final PString specificCatalog = createString("SPECIFIC_CATALOG");

    public final PString specificName = createString("SPECIFIC_NAME");

    public final PString specificSchema = createString("SPECIFIC_SCHEMA");

    public QSystemCheckRoutineUsage(String variable) {
        super(QSystemCheckRoutineUsage.class, forVariable(variable));
    }

    public QSystemCheckRoutineUsage(PEntity<? extends QSystemCheckRoutineUsage> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCheckRoutineUsage(PathMetadata<?> metadata) {
        super(QSystemCheckRoutineUsage.class, metadata);
    }

}

