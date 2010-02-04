package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemUdtattributes is a Querydsl query type for QSystemUdtattributes
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_UDTATTRIBUTES")
public class QSystemUdtattributes extends PEntity<QSystemUdtattributes> {

    public final PString attrDef = createString("ATTR_DEF");

    public final PString attrName = createString("ATTR_NAME");

    public final PNumber<Integer> attrSize = createNumber("ATTR_SIZE", Integer.class);

    public final PString attrTypeName = createString("ATTR_TYPE_NAME");

    public final PNumber<Integer> charOctetLength = createNumber("CHAR_OCTET_LENGTH", Integer.class);

    public final PNumber<Short> dataType = createNumber("DATA_TYPE", Short.class);

    public final PNumber<Integer> decimalDigits = createNumber("DECIMAL_DIGITS", Integer.class);

    public final PString isNullable = createString("IS_NULLABLE");

    public final PNumber<Integer> nullable = createNumber("NULLABLE", Integer.class);

    public final PNumber<Integer> numPrecRadix = createNumber("NUM_PREC_RADIX", Integer.class);

    public final PNumber<Integer> ordinalPosition = createNumber("ORDINAL_POSITION", Integer.class);

    public final PString remarks = createString("REMARKS");

    public final PString scopeCatalog = createString("SCOPE_CATALOG");

    public final PString scopeSchema = createString("SCOPE_SCHEMA");

    public final PString scopeTable = createString("SCOPE_TABLE");

    public final PNumber<Short> sourceDataType = createNumber("SOURCE_DATA_TYPE", Short.class);

    public final PNumber<Integer> sqlDataType = createNumber("SQL_DATA_TYPE", Integer.class);

    public final PNumber<Integer> sqlDatetimeSub = createNumber("SQL_DATETIME_SUB", Integer.class);

    public final PString typeCat = createString("TYPE_CAT");

    public final PString typeName = createString("TYPE_NAME");

    public final PString typeSchem = createString("TYPE_SCHEM");

    public QSystemUdtattributes(String variable) {
        super(QSystemUdtattributes.class, forVariable(variable));
    }

    public QSystemUdtattributes(PEntity<? extends QSystemUdtattributes> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemUdtattributes(PathMetadata<?> metadata) {
        super(QSystemUdtattributes.class, metadata);
    }

}

