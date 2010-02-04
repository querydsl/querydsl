package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemViewTableUsage is a Querydsl query type for QSystemViewTableUsage
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_VIEW_TABLE_USAGE")
public class QSystemViewTableUsage extends PEntity<QSystemViewTableUsage> {

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchema = createString("TABLE_SCHEMA");

    public final PString viewCatalog = createString("VIEW_CATALOG");

    public final PString viewName = createString("VIEW_NAME");

    public final PString viewSchema = createString("VIEW_SCHEMA");

    public QSystemViewTableUsage(String variable) {
        super(QSystemViewTableUsage.class, forVariable(variable));
    }

    public QSystemViewTableUsage(PEntity<? extends QSystemViewTableUsage> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemViewTableUsage(PathMetadata<?> metadata) {
        super(QSystemViewTableUsage.class, metadata);
    }

}

