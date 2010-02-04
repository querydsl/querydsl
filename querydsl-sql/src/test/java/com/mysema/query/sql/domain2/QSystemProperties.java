package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemProperties is a Querydsl query type for QSystemProperties
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_PROPERTIES")
public class QSystemProperties extends PEntity<QSystemProperties> {

    public final PString propertyClass = createString("PROPERTY_CLASS");

    public final PString propertyName = createString("PROPERTY_NAME");

    public final PString propertyNamespace = createString("PROPERTY_NAMESPACE");

    public final PString propertyScope = createString("PROPERTY_SCOPE");

    public final PString propertyValue = createString("PROPERTY_VALUE");

    public QSystemProperties(String variable) {
        super(QSystemProperties.class, forVariable(variable));
    }

    public QSystemProperties(PEntity<? extends QSystemProperties> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemProperties(PathMetadata<?> metadata) {
        super(QSystemProperties.class, metadata);
    }

}

