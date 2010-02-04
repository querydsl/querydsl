package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSurvey is a Querydsl query type for QSurvey
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SURVEY")
public class QSurvey extends PEntity<QSurvey> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public QSurvey(String variable) {
        super(QSurvey.class, forVariable(variable));
    }

    public QSurvey(PEntity<? extends QSurvey> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSurvey(PathMetadata<?> metadata) {
        super(QSurvey.class, metadata);
    }

}

