package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemAlltypeinfo is a Querydsl query type for QSystemAlltypeinfo
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_ALLTYPEINFO")
public class QSystemAlltypeinfo extends PEntity<QSystemAlltypeinfo> {

    public final PBoolean asProcCol = createBoolean("AS_PROC_COL");

    public final PBoolean asTabCol = createBoolean("AS_TAB_COL");

    public final PBoolean autoIncrement = createBoolean("AUTO_INCREMENT");

    public final PBoolean caseSensitive = createBoolean("CASE_SENSITIVE");

    public final PString colStClsName = createString("COL_ST_CLS_NAME");

    public final PBoolean colStIsSup = createBoolean("COL_ST_IS_SUP");

    public final PString createParams = createString("CREATE_PARAMS");

    public final PString cstMapClsName = createString("CST_MAP_CLS_NAME");

    public final PBoolean cstMapIsSup = createBoolean("CST_MAP_IS_SUP");

    public final PNumber<Short> dataType = createNumber("DATA_TYPE", Short.class);

    public final PNumber<Integer> defOrFixedScale = createNumber("DEF_OR_FIXED_SCALE", Integer.class);

    public final PBoolean fixedPrecScale = createBoolean("FIXED_PREC_SCALE");

    public final PNumber<Integer> intervalPrecision = createNumber("INTERVAL_PRECISION", Integer.class);

    public final PString literalPrefix = createString("LITERAL_PREFIX");

    public final PString literalSuffix = createString("LITERAL_SUFFIX");

    public final PString localTypeName = createString("LOCAL_TYPE_NAME");

    public final PNumber<Long> maxPrecAct = createNumber("MAX_PREC_ACT", Long.class);

    public final PNumber<Integer> maxScaleAct = createNumber("MAX_SCALE_ACT", Integer.class);

    public final PNumber<Short> maximumScale = createNumber("MAXIMUM_SCALE", Short.class);

    public final PNumber<Long> mcolAct = createNumber("MCOL_ACT", Long.class);

    public final PNumber<Integer> mcolJdbc = createNumber("MCOL_JDBC", Integer.class);

    public final PNumber<Integer> minScaleAct = createNumber("MIN_SCALE_ACT", Integer.class);

    public final PNumber<Short> minimumScale = createNumber("MINIMUM_SCALE", Short.class);

    public final PNumber<Short> nullable = createNumber("NULLABLE", Short.class);

    public final PNumber<Integer> numPrecRadix = createNumber("NUM_PREC_RADIX", Integer.class);

    public final PNumber<Integer> precision = createNumber("PRECISION", Integer.class);

    public final PString remarks = createString("REMARKS");

    public final PNumber<Short> searchable = createNumber("SEARCHABLE", Short.class);

    public final PNumber<Integer> sqlDataType = createNumber("SQL_DATA_TYPE", Integer.class);

    public final PNumber<Integer> sqlDatetimeSub = createNumber("SQL_DATETIME_SUB", Integer.class);

    public final PString stdMapClsName = createString("STD_MAP_CLS_NAME");

    public final PBoolean stdMapIsSup = createBoolean("STD_MAP_IS_SUP");

    public final PString typeName = createString("TYPE_NAME");

    public final PNumber<Integer> typeSub = createNumber("TYPE_SUB", Integer.class);

    public final PBoolean unsignedAttribute = createBoolean("UNSIGNED_ATTRIBUTE");

    public QSystemAlltypeinfo(String variable) {
        super(QSystemAlltypeinfo.class, forVariable(variable));
    }

    public QSystemAlltypeinfo(PEntity<? extends QSystemAlltypeinfo> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemAlltypeinfo(PathMetadata<?> metadata) {
        super(QSystemAlltypeinfo.class, metadata);
    }

}

