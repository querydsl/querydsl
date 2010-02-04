package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTables is a Querydsl query type for QSystemTables
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TABLES")
public class QSystemTables extends PEntity<QSystemTables> {

    public final PString hsqldbType = createString("HSQLDB_TYPE");

    public final PBoolean readOnly = createBoolean("READ_ONLY");

    public final PString refGeneration = createString("REF_GENERATION");

    public final PString remarks = createString("REMARKS");

    public final PString selfReferencingColName = createString("SELF_REFERENCING_COL_NAME");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PString tableType = createString("TABLE_TYPE");

    public final PString typeCat = createString("TYPE_CAT");

    public final PString typeName = createString("TYPE_NAME");

    public final PString typeSchem = createString("TYPE_SCHEM");

    public QSystemTables(String variable) {
        super(QSystemTables.class, forVariable(variable));
    }

    public QSystemTables(PEntity<? extends QSystemTables> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTables(PathMetadata<?> metadata) {
        super(QSystemTables.class, metadata);
    }

}

