package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemAuthorizations is a Querydsl query type for QSystemAuthorizations
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_AUTHORIZATIONS")
public class QSystemAuthorizations extends PEntity<QSystemAuthorizations> {

    public final PString authorizationName = createString("AUTHORIZATION_NAME");

    public final PString authorizationType = createString("AUTHORIZATION_TYPE");

    public QSystemAuthorizations(String variable) {
        super(QSystemAuthorizations.class, forVariable(variable));
    }

    public QSystemAuthorizations(PEntity<? extends QSystemAuthorizations> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemAuthorizations(PathMetadata<?> metadata) {
        super(QSystemAuthorizations.class, metadata);
    }

}

