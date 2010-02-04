package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemBestrowidentifier is a Querydsl query type for QSystemBestrowidentifier
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_BESTROWIDENTIFIER")
public class QSystemBestrowidentifier extends PEntity<QSystemBestrowidentifier> {

    public final PNumber<Integer> bufferLength = createNumber("BUFFER_LENGTH", Integer.class);

    public final PString columnName = createString("COLUMN_NAME");

    public final PNumber<Integer> columnSize = createNumber("COLUMN_SIZE", Integer.class);

    public final PNumber<Short> dataType = createNumber("DATA_TYPE", Short.class);

    public final PNumber<Short> decimalDigits = createNumber("DECIMAL_DIGITS", Short.class);

    public final PBoolean inKey = createBoolean("IN_KEY");

    public final PNumber<Short> nullable = createNumber("NULLABLE", Short.class);

    public final PNumber<Short> pseudoColumn = createNumber("PSEUDO_COLUMN", Short.class);

    public final PNumber<Short> scope = createNumber("SCOPE", Short.class);

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PString typeName = createString("TYPE_NAME");

    public QSystemBestrowidentifier(String variable) {
        super(QSystemBestrowidentifier.class, forVariable(variable));
    }

    public QSystemBestrowidentifier(PEntity<? extends QSystemBestrowidentifier> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemBestrowidentifier(PathMetadata<?> metadata) {
        super(QSystemBestrowidentifier.class, metadata);
    }

}

