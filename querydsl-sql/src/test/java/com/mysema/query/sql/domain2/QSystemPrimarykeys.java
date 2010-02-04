package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemPrimarykeys is a Querydsl query type for QSystemPrimarykeys
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_PRIMARYKEYS")
public class QSystemPrimarykeys extends PEntity<QSystemPrimarykeys> {

    public final PString columnName = createString("COLUMN_NAME");

    public final PNumber<Short> keySeq = createNumber("KEY_SEQ", Short.class);

    public final PString pkName = createString("PK_NAME");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public QSystemPrimarykeys(String variable) {
        super(QSystemPrimarykeys.class, forVariable(variable));
    }

    public QSystemPrimarykeys(PEntity<? extends QSystemPrimarykeys> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemPrimarykeys(PathMetadata<?> metadata) {
        super(QSystemPrimarykeys.class, metadata);
    }

}

