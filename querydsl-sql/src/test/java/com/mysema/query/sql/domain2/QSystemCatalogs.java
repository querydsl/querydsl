package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCatalogs is a Querydsl query type for QSystemCatalogs
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_CATALOGS")
public class QSystemCatalogs extends PEntity<QSystemCatalogs> {

    public final PString tableCat = createString("TABLE_CAT");

    public QSystemCatalogs(String variable) {
        super(QSystemCatalogs.class, forVariable(variable));
    }

    public QSystemCatalogs(PEntity<? extends QSystemCatalogs> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCatalogs(PathMetadata<?> metadata) {
        super(QSystemCatalogs.class, metadata);
    }

}

