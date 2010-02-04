package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemViews is a Querydsl query type for QSystemViews
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_VIEWS")
public class QSystemViews extends PEntity<QSystemViews> {

    public final PString checkOption = createString("CHECK_OPTION");

    public final PString isUpdatable = createString("IS_UPDATABLE");

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchema = createString("TABLE_SCHEMA");

    public final PBoolean valid = createBoolean("VALID");

    public final PString viewDefinition = createString("VIEW_DEFINITION");

    public QSystemViews(String variable) {
        super(QSystemViews.class, forVariable(variable));
    }

    public QSystemViews(PEntity<? extends QSystemViews> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemViews(PathMetadata<?> metadata) {
        super(QSystemViews.class, metadata);
    }

}

