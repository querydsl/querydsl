package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemSchemata is a Querydsl query type for QSystemSchemata
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_SCHEMATA")
public class QSystemSchemata extends PEntity<QSystemSchemata> {

    public final PString catalogName = createString("CATALOG_NAME");

    public final PString defaultCharacterSetCatalog = createString("DEFAULT_CHARACTER_SET_CATALOG");

    public final PString defaultCharacterSetName = createString("DEFAULT_CHARACTER_SET_NAME");

    public final PString defaultCharacterSetSchema = createString("DEFAULT_CHARACTER_SET_SCHEMA");

    public final PString schemaName = createString("SCHEMA_NAME");

    public final PString schemaOwner = createString("SCHEMA_OWNER");

    public final PString sqlPath = createString("SQL_PATH");

    public QSystemSchemata(String variable) {
        super(QSystemSchemata.class, forVariable(variable));
    }

    public QSystemSchemata(PEntity<? extends QSystemSchemata> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemSchemata(PathMetadata<?> metadata) {
        super(QSystemSchemata.class, metadata);
    }

}

