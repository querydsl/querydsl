package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemSequences is a Querydsl query type for QSystemSequences
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_SEQUENCES")
public class QSystemSequences extends PEntity<QSystemSequences> {

    public final PString cycleOption = createString("CYCLE_OPTION");

    public final PString dtdIdentifier = createString("DTD_IDENTIFIER");

    public final PString increment = createString("INCREMENT");

    public final PString maximumValue = createString("MAXIMUM_VALUE");

    public final PString minimumValue = createString("MINIMUM_VALUE");

    public final PString sequenceCatalog = createString("SEQUENCE_CATALOG");

    public final PString sequenceName = createString("SEQUENCE_NAME");

    public final PString sequenceSchema = createString("SEQUENCE_SCHEMA");

    public final PString startWith = createString("START_WITH");

    public QSystemSequences(String variable) {
        super(QSystemSequences.class, forVariable(variable));
    }

    public QSystemSequences(PEntity<? extends QSystemSequences> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemSequences(PathMetadata<?> metadata) {
        super(QSystemSequences.class, metadata);
    }

}

