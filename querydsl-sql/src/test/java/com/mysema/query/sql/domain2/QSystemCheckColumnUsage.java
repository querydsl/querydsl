package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCheckColumnUsage is a Querydsl query type for QSystemCheckColumnUsage
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CHECK_COLUMN_USAGE")
public class QSystemCheckColumnUsage extends PEntity<QSystemCheckColumnUsage> {

    public final PString columnName = createString("COLUMN_NAME");

    public final PString constraintCatalog = createString("CONSTRAINT_CATALOG");

    public final PString constraintName = createString("CONSTRAINT_NAME");

    public final PString constraintSchema = createString("CONSTRAINT_SCHEMA");

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchema = createString("TABLE_SCHEMA");

    public QSystemCheckColumnUsage(String variable) {
        super(QSystemCheckColumnUsage.class, forVariable(variable));
    }

    public QSystemCheckColumnUsage(PEntity<? extends QSystemCheckColumnUsage> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCheckColumnUsage(PathMetadata<?> metadata) {
        super(QSystemCheckColumnUsage.class, metadata);
    }

}

