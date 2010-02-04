package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCheckTableUsage is a Querydsl query type for QSystemCheckTableUsage
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CHECK_TABLE_USAGE")
public class QSystemCheckTableUsage extends PEntity<QSystemCheckTableUsage> {

    public final PString constraintCatalog = createString("CONSTRAINT_CATALOG");

    public final PString constraintName = createString("CONSTRAINT_NAME");

    public final PString constraintSchema = createString("CONSTRAINT_SCHEMA");

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchema = createString("TABLE_SCHEMA");

    public QSystemCheckTableUsage(String variable) {
        super(QSystemCheckTableUsage.class, forVariable(variable));
    }

    public QSystemCheckTableUsage(PEntity<? extends QSystemCheckTableUsage> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCheckTableUsage(PathMetadata<?> metadata) {
        super(QSystemCheckTableUsage.class, metadata);
    }

}

