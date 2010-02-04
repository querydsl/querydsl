package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemCollations is a Querydsl query type for QSystemCollations
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_COLLATIONS")
public class QSystemCollations extends PEntity<QSystemCollations> {

    public final PString characterRepertoireName = createString("CHARACTER_REPERTOIRE_NAME");

    public final PString collationCatalog = createString("COLLATION_CATALOG");

    public final PString collationDefinition = createString("COLLATION_DEFINITION");

    public final PString collationDictionary = createString("COLLATION_DICTIONARY");

    public final PString collationName = createString("COLLATION_NAME");

    public final PString collationSchema = createString("COLLATION_SCHEMA");

    public final PString collationType = createString("COLLATION_TYPE");

    public final PString padAttribute = createString("PAD_ATTRIBUTE");

    public QSystemCollations(String variable) {
        super(QSystemCollations.class, forVariable(variable));
    }

    public QSystemCollations(PEntity<? extends QSystemCollations> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemCollations(PathMetadata<?> metadata) {
        super(QSystemCollations.class, metadata);
    }

}

