package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemSchemas is a Querydsl query type for QSystemSchemas
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_SCHEMAS")
public class QSystemSchemas extends PEntity<QSystemSchemas> {

    public final PBoolean isDefault = createBoolean("IS_DEFAULT");

    public final PString tableCatalog = createString("TABLE_CATALOG");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public QSystemSchemas(String variable) {
        super(QSystemSchemas.class, forVariable(variable));
    }

    public QSystemSchemas(PEntity<? extends QSystemSchemas> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemSchemas(PathMetadata<?> metadata) {
        super(QSystemSchemas.class, metadata);
    }

}

