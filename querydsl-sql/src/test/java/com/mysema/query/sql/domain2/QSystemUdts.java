package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemUdts is a Querydsl query type for QSystemUdts
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_UDTS")
public class QSystemUdts extends PEntity<QSystemUdts> {

    public final PNumber<Short> baseType = createNumber("BASE_TYPE", Short.class);

    public final PString className = createString("CLASS_NAME");

    public final PString dataType = createString("DATA_TYPE");

    public final PString remarks = createString("REMARKS");

    public final PString typeCat = createString("TYPE_CAT");

    public final PString typeName = createString("TYPE_NAME");

    public final PString typeSchem = createString("TYPE_SCHEM");

    public QSystemUdts(String variable) {
        super(QSystemUdts.class, forVariable(variable));
    }

    public QSystemUdts(PEntity<? extends QSystemUdts> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemUdts(PathMetadata<?> metadata) {
        super(QSystemUdts.class, metadata);
    }

}

