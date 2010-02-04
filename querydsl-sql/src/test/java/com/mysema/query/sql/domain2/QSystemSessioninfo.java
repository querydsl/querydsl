package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemSessioninfo is a Querydsl query type for QSystemSessioninfo
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_SESSIONINFO")
public class QSystemSessioninfo extends PEntity<QSystemSessioninfo> {

    public final PString key = createString("KEY");

    public final PString value = createString("VALUE");

    public QSystemSessioninfo(String variable) {
        super(QSystemSessioninfo.class, forVariable(variable));
    }

    public QSystemSessioninfo(PEntity<? extends QSystemSessioninfo> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemSessioninfo(PathMetadata<?> metadata) {
        super(QSystemSessioninfo.class, metadata);
    }

}

