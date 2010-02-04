package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemColumns is a Querydsl query type for QSystemColumns
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_COLUMNS")
public class QSystemColumns extends PEntity<QSystemColumns> {

    public final PNumber<Integer> bufferLength = createNumber("BUFFER_LENGTH", Integer.class);

    public final PNumber<Integer> charOctetLength = createNumber("CHAR_OCTET_LENGTH", Integer.class);

    public final PString columnDef = createString("COLUMN_DEF");

    public final PString columnName = createString("COLUMN_NAME");

    public final PNumber<Integer> columnSize = createNumber("COLUMN_SIZE", Integer.class);

    public final PNumber<Short> dataType = createNumber("DATA_TYPE", Short.class);

    public final PNumber<Integer> decimalDigits = createNumber("DECIMAL_DIGITS", Integer.class);

    public final PString isNullable = createString("IS_NULLABLE");

    public final PNumber<Integer> nullable = createNumber("NULLABLE", Integer.class);

    public final PNumber<Integer> numPrecRadix = createNumber("NUM_PREC_RADIX", Integer.class);

    public final PNumber<Integer> ordinalPosition = createNumber("ORDINAL_POSITION", Integer.class);

    public final PString remarks = createString("REMARKS");

    public final PString scopeCatlog = createString("SCOPE_CATLOG");

    public final PString scopeSchema = createString("SCOPE_SCHEMA");

    public final PString scopeTable = createString("SCOPE_TABLE");

    public final PString sourceDataType = createString("SOURCE_DATA_TYPE");

    public final PNumber<Integer> sqlDataType = createNumber("SQL_DATA_TYPE", Integer.class);

    public final PNumber<Integer> sqlDatetimeSub = createNumber("SQL_DATETIME_SUB", Integer.class);

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PString typeName = createString("TYPE_NAME");

    public final PNumber<Integer> typeSub = createNumber("TYPE_SUB", Integer.class);

    public QSystemColumns(String variable) {
        super(QSystemColumns.class, forVariable(variable));
    }

    public QSystemColumns(PEntity<? extends QSystemColumns> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemColumns(PathMetadata<?> metadata) {
        super(QSystemColumns.class, metadata);
    }

}

