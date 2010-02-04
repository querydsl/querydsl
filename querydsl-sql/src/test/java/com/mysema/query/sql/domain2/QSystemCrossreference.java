package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCrossreference is a Querydsl query type for QSystemCrossreference
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CROSSREFERENCE")
public class QSystemCrossreference extends PEntity<QSystemCrossreference> {

    public final PNumber<Short> deferrability = createNumber("DEFERRABILITY", Short.class);

    public final PNumber<Short> deleteRule = createNumber("DELETE_RULE", Short.class);

    public final PString fkName = createString("FK_NAME");

    public final PString fkcolumnName = createString("FKCOLUMN_NAME");

    public final PString fktableCat = createString("FKTABLE_CAT");

    public final PString fktableName = createString("FKTABLE_NAME");

    public final PString fktableSchem = createString("FKTABLE_SCHEM");

    public final PNumber<Short> keySeq = createNumber("KEY_SEQ", Short.class);

    public final PString pkName = createString("PK_NAME");

    public final PString pkcolumnName = createString("PKCOLUMN_NAME");

    public final PString pktableCat = createString("PKTABLE_CAT");

    public final PString pktableName = createString("PKTABLE_NAME");

    public final PString pktableSchem = createString("PKTABLE_SCHEM");

    public final PNumber<Short> updateRule = createNumber("UPDATE_RULE", Short.class);

    public QSystemCrossreference(String variable) {
        super(QSystemCrossreference.class, forVariable(variable));
    }

    public QSystemCrossreference(PEntity<? extends QSystemCrossreference> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCrossreference(PathMetadata<?> metadata) {
        super(QSystemCrossreference.class, metadata);
    }

}

