package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemVersioncolumns is a Querydsl query type for QSystemVersioncolumns
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_VERSIONCOLUMNS")
public class QSystemVersioncolumns extends PEntity<QSystemVersioncolumns> {

    public final PNumber<Integer> bufferLength = createNumber("BUFFER_LENGTH", Integer.class);

    public final PString columnName = createString("COLUMN_NAME");

    public final PNumber<Short> columnSize = createNumber("COLUMN_SIZE", Short.class);

    public final PNumber<Short> dataType = createNumber("DATA_TYPE", Short.class);

    public final PNumber<Short> decimalDigits = createNumber("DECIMAL_DIGITS", Short.class);

    public final PNumber<Short> pseudoColumn = createNumber("PSEUDO_COLUMN", Short.class);

    public final PNumber<Integer> scope = createNumber("SCOPE", Integer.class);

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PString typeName = createString("TYPE_NAME");

    public QSystemVersioncolumns(String variable) {
        super(QSystemVersioncolumns.class, forVariable(variable));
    }

    public QSystemVersioncolumns(PEntity<? extends QSystemVersioncolumns> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemVersioncolumns(PathMetadata<?> metadata) {
        super(QSystemVersioncolumns.class, metadata);
    }

}

