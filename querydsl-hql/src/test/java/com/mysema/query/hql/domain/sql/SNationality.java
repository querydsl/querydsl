package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SNationality is a Querydsl query type for SNationality
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="NATIONALITY")
public class SNationality extends PEntity<SNationality> {

    public final PNumber<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable));
    }

    public SNationality(PEntity<? extends SNationality> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata);
    }

}

