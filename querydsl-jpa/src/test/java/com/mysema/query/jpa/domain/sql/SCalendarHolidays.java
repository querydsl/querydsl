package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCalendarHolidays is a Querydsl query type for SCalendarHolidays
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCalendarHolidays extends com.mysema.query.sql.RelationalPathBase<SCalendarHolidays> {

    private static final long serialVersionUID = 850508650;

    public static final SCalendarHolidays CalendarHolidays = new SCalendarHolidays("Calendar_holidays");

    public final NumberPath<Integer> calendarId = createNumber("Calendar_id", Integer.class);

    public final DatePath<java.sql.Date> holidays = createDate("holidays", java.sql.Date.class);

    public final StringPath holidaysKEY = createString("holidays_KEY");

    public final com.mysema.query.sql.PrimaryKey<SCalendarHolidays> primary = createPrimaryKey(calendarId, holidaysKEY);

    public final com.mysema.query.sql.ForeignKey<SCalendar> fk31ce1edc591ebbc = createForeignKey(calendarId, "id");

    public SCalendarHolidays(String variable) {
        super(SCalendarHolidays.class, forVariable(variable), "null", "Calendar_holidays");
    }

    public SCalendarHolidays(Path<? extends SCalendarHolidays> path) {
        super(path.getType(), path.getMetadata(), "null", "Calendar_holidays");
    }

    public SCalendarHolidays(PathMetadata<?> metadata) {
        super(SCalendarHolidays.class, metadata, "null", "Calendar_holidays");
    }

}

