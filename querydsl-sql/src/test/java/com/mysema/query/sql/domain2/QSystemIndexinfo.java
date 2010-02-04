package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemIndexinfo is a Querydsl query type for QSystemIndexinfo
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_INDEXINFO")
public class QSystemIndexinfo extends PEntity<QSystemIndexinfo> {

    public final PString ascOrDesc = createString("ASC_OR_DESC");

    public final PNumber<Integer> cardinality = createNumber("CARDINALITY", Integer.class);

    public final PString columnName = createString("COLUMN_NAME");

    public final PString filterCondition = createString("FILTER_CONDITION");

    public final PString indexName = createString("INDEX_NAME");

    public final PString indexQualifier = createString("INDEX_QUALIFIER");

    public final PBoolean nonUnique = createBoolean("NON_UNIQUE");

    public final PNumber<Short> ordinalPosition = createNumber("ORDINAL_POSITION", Short.class);

    public final PNumber<Integer> pages = createNumber("PAGES", Integer.class);

    public final PNumber<Integer> rowCardinality = createNumber("ROW_CARDINALITY", Integer.class);

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PNumber<Short> type = createNumber("TYPE", Short.class);

    public QSystemIndexinfo(String variable) {
        super(QSystemIndexinfo.class, forVariable(variable));
    }

    public QSystemIndexinfo(PEntity<? extends QSystemIndexinfo> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemIndexinfo(PathMetadata<?> metadata) {
        super(QSystemIndexinfo.class, metadata);
    }

}

