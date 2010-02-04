package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemViewColumnUsage is a Querydsl query type for QSystemViewColumnUsage
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_VIEW_COLUMN_USAGE")
public class QSystemViewColumnUsage extends PEntity<QSystemViewColumnUsage> {

    public final PString columnName = createString("COLUMN_NAME");

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchema = createString("TABLE_SCHEMA");

    public final PString viewCatalog = createString("VIEW_CATALOG");

    public final PString viewName = createString("VIEW_NAME");

    public final PString viewSchema = createString("VIEW_SCHEMA");

    public QSystemViewColumnUsage(String variable) {
        super(QSystemViewColumnUsage.class, forVariable(variable));
    }

    public QSystemViewColumnUsage(PEntity<? extends QSystemViewColumnUsage> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemViewColumnUsage(PathMetadata<?> metadata) {
        super(QSystemViewColumnUsage.class, metadata);
    }

}

