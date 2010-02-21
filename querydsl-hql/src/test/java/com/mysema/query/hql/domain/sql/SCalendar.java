package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SCalendar is a Querydsl query type for SCalendar
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="CALENDAR")
public class SCalendar extends PEntity<SCalendar> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public SCalendar(String variable) {
        super(SCalendar.class, forVariable(variable));
    }

    public SCalendar(PEntity<? extends SCalendar> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCalendar(PathMetadata<?> metadata) {
        super(SCalendar.class, metadata);
    }

}

