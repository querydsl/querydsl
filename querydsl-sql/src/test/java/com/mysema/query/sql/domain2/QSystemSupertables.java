package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemSupertables is a Querydsl query type for QSystemSupertables
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_SUPERTABLES")
public class QSystemSupertables extends PEntity<QSystemSupertables> {

    public final PString supertableName = createString("SUPERTABLE_NAME");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public QSystemSupertables(String variable) {
        super(QSystemSupertables.class, forVariable(variable));
    }

    public QSystemSupertables(PEntity<? extends QSystemSupertables> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemSupertables(PathMetadata<?> metadata) {
        super(QSystemSupertables.class, metadata);
    }

}

