package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemSupertypes is a Querydsl query type for QSystemSupertypes
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_SUPERTYPES")
public class QSystemSupertypes extends PEntity<QSystemSupertypes> {

    public final PString supertypeCat = createString("SUPERTYPE_CAT");

    public final PString supertypeName = createString("SUPERTYPE_NAME");

    public final PString supertypeSchem = createString("SUPERTYPE_SCHEM");

    public final PString typeCat = createString("TYPE_CAT");

    public final PString typeName = createString("TYPE_NAME");

    public final PString typeSchem = createString("TYPE_SCHEM");

    public QSystemSupertypes(String variable) {
        super(QSystemSupertypes.class, forVariable(variable));
    }

    public QSystemSupertypes(PEntity<? extends QSystemSupertypes> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemSupertypes(PathMetadata<?> metadata) {
        super(QSystemSupertypes.class, metadata);
    }

}

