package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SCalendarHolidays is a Querydsl query type for SCalendarHolidays
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="CALENDAR_HOLIDAYS")
public class SCalendarHolidays extends PEntity<SCalendarHolidays> {

    public final PNumber<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final PComparable<java.util.Date> element = createComparable("ELEMENT", java.util.Date.class);

    public final PString mapkey = createString("MAPKEY");

    public SCalendarHolidays(String variable) {
        super(SCalendarHolidays.class, forVariable(variable));
    }

    public SCalendarHolidays(PEntity<? extends SCalendarHolidays> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCalendarHolidays(PathMetadata<?> metadata) {
        super(SCalendarHolidays.class, metadata);
    }

}

