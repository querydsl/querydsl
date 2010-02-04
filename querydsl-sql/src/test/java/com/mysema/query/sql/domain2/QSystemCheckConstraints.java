package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCheckConstraints is a Querydsl query type for QSystemCheckConstraints
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CHECK_CONSTRAINTS")
public class QSystemCheckConstraints extends PEntity<QSystemCheckConstraints> {

    public final PString checkClause = createString("CHECK_CLAUSE");

    public final PString constraintCatalog = createString("CONSTRAINT_CATALOG");

    public final PString constraintName = createString("CONSTRAINT_NAME");

    public final PString constraintSchema = createString("CONSTRAINT_SCHEMA");

    public QSystemCheckConstraints(String variable) {
        super(QSystemCheckConstraints.class, forVariable(variable));
    }

    public QSystemCheckConstraints(PEntity<? extends QSystemCheckConstraints> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCheckConstraints(PathMetadata<?> metadata) {
        super(QSystemCheckConstraints.class, metadata);
    }

}

