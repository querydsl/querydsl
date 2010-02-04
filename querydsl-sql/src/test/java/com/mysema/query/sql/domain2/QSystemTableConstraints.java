package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTableConstraints is a Querydsl query type for QSystemTableConstraints
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TABLE_CONSTRAINTS")
public class QSystemTableConstraints extends PEntity<QSystemTableConstraints> {

    public final PString constraintCatalog = createString("CONSTRAINT_CATALOG");

    public final PString constraintName = createString("CONSTRAINT_NAME");

    public final PString constraintSchema = createString("CONSTRAINT_SCHEMA");

    public final PString constraintType = createString("CONSTRAINT_TYPE");

    public final PString initiallyDeferred = createString("INITIALLY_DEFERRED");

    public final PString isDeferrable = createString("IS_DEFERRABLE");

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchema = createString("TABLE_SCHEMA");

    public QSystemTableConstraints(String variable) {
        super(QSystemTableConstraints.class, forVariable(variable));
    }

    public QSystemTableConstraints(PEntity<? extends QSystemTableConstraints> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTableConstraints(PathMetadata<?> metadata) {
        super(QSystemTableConstraints.class, metadata);
    }

}

