package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemAliases is a Querydsl query type for QSystemAliases
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_ALIASES")
public class QSystemAliases extends PEntity<QSystemAliases> {

    public final PString alias = createString("ALIAS");

    public final PString aliasCat = createString("ALIAS_CAT");

    public final PString aliasSchem = createString("ALIAS_SCHEM");

    public final PString objectCat = createString("OBJECT_CAT");

    public final PString objectName = createString("OBJECT_NAME");

    public final PString objectSchem = createString("OBJECT_SCHEM");

    public final PString objectType = createString("OBJECT_TYPE");

    public QSystemAliases(String variable) {
        super(QSystemAliases.class, forVariable(variable));
    }

    public QSystemAliases(PEntity<? extends QSystemAliases> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemAliases(PathMetadata<?> metadata) {
        super(QSystemAliases.class, metadata);
    }

}

