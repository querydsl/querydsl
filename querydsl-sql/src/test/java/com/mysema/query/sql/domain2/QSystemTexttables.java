package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTexttables is a Querydsl query type for QSystemTexttables
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TEXTTABLES")
public class QSystemTexttables extends PEntity<QSystemTexttables> {

    public final PString dataSourceDefintion = createString("DATA_SOURCE_DEFINTION");

    public final PString fieldSeparator = createString("FIELD_SEPARATOR");

    public final PString fileEncoding = createString("FILE_ENCODING");

    public final PString filePath = createString("FILE_PATH");

    public final PBoolean isAllQuoted = createBoolean("IS_ALL_QUOTED");

    public final PBoolean isDesc = createBoolean("IS_DESC");

    public final PBoolean isIgnoreFirst = createBoolean("IS_IGNORE_FIRST");

    public final PBoolean isQuoted = createBoolean("IS_QUOTED");

    public final PString longvarcharSeparator = createString("LONGVARCHAR_SEPARATOR");

    public final PString tableCat = createString("TABLE_CAT");

    public final PString tableName = createString("TABLE_NAME");

    public final PString tableSchem = createString("TABLE_SCHEM");

    public final PString varcharSeparator = createString("VARCHAR_SEPARATOR");

    public QSystemTexttables(String variable) {
        super(QSystemTexttables.class, forVariable(variable));
    }

    public QSystemTexttables(PEntity<? extends QSystemTexttables> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTexttables(PathMetadata<?> metadata) {
        super(QSystemTexttables.class, metadata);
    }

}

