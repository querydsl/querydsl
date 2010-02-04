package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemTabletypes is a Querydsl query type for QSystemTabletypes
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_TABLETYPES")
public class QSystemTabletypes extends PEntity<QSystemTabletypes> {

    public final PString tableType = createString("TABLE_TYPE");

    public QSystemTabletypes(String variable) {
        super(QSystemTabletypes.class, forVariable(variable));
    }

    public QSystemTabletypes(PEntity<? extends QSystemTabletypes> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemTabletypes(PathMetadata<?> metadata) {
        super(QSystemTabletypes.class, metadata);
    }

}

