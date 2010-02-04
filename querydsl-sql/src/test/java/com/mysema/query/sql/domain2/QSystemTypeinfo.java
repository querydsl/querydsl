package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTypeinfo is a Querydsl query type for QSystemTypeinfo
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TYPEINFO")
public class QSystemTypeinfo extends PEntity<QSystemTypeinfo> {

    public final PBoolean autoIncrement = createBoolean("AUTO_INCREMENT");

    public final PBoolean caseSensitive = createBoolean("CASE_SENSITIVE");

    public final PString createParams = createString("CREATE_PARAMS");

    public final PNumber<Short> dataType = createNumber("DATA_TYPE", Short.class);

    public final PBoolean fixedPrecScale = createBoolean("FIXED_PREC_SCALE");

    public final PString literalPrefix = createString("LITERAL_PREFIX");

    public final PString literalSuffix = createString("LITERAL_SUFFIX");

    public final PString localTypeName = createString("LOCAL_TYPE_NAME");

    public final PNumber<Short> maximumScale = createNumber("MAXIMUM_SCALE", Short.class);

    public final PNumber<Short> minimumScale = createNumber("MINIMUM_SCALE", Short.class);

    public final PNumber<Short> nullable = createNumber("NULLABLE", Short.class);

    public final PNumber<Integer> numPrecRadix = createNumber("NUM_PREC_RADIX", Integer.class);

    public final PNumber<Integer> precision = createNumber("PRECISION", Integer.class);

    public final PNumber<Short> searchable = createNumber("SEARCHABLE", Short.class);

    public final PNumber<Integer> sqlDataType = createNumber("SQL_DATA_TYPE", Integer.class);

    public final PNumber<Integer> sqlDatetimeSub = createNumber("SQL_DATETIME_SUB", Integer.class);

    public final PString typeName = createString("TYPE_NAME");

    public final PNumber<Integer> typeSub = createNumber("TYPE_SUB", Integer.class);

    public final PBoolean unsignedAttribute = createBoolean("UNSIGNED_ATTRIBUTE");

    public QSystemTypeinfo(String variable) {
        super(QSystemTypeinfo.class, forVariable(variable));
    }

    public QSystemTypeinfo(PEntity<? extends QSystemTypeinfo> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTypeinfo(PathMetadata<?> metadata) {
        super(QSystemTypeinfo.class, metadata);
    }

}

