package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCalendarHolidays is a Querydsl query type for SCalendarHolidays
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCalendarHolidays extends com.mysema.query.sql.RelationalPathBase<SCalendarHolidays> {

    private static final long serialVersionUID = 850508650;

    public static final SCalendarHolidays calendarHolidays = new SCalendarHolidays("CALENDAR_HOLIDAYS");

    public final NumberPath<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final DatePath<java.sql.Date> holidays = createDate("HOLIDAYS", java.sql.Date.class);

    public final StringPath holidaysKey = createString("HOLIDAYS_KEY");

    public final com.mysema.query.sql.PrimaryKey<SCalendarHolidays> sql120219232318470 = createPrimaryKey(calendarId, holidaysKey);

    public final com.mysema.query.sql.ForeignKey<SCalendar> fk31ce1edc591ebbc = createForeignKey(calendarId, "ID");

    public SCalendarHolidays(String variable) {
        super(SCalendarHolidays.class, forVariable(variable), "APP", "CALENDAR_HOLIDAYS");
    }

    public SCalendarHolidays(Path<? extends SCalendarHolidays> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CALENDAR_HOLIDAYS");
    }

    public SCalendarHolidays(PathMetadata<?> metadata) {
        super(SCalendarHolidays.class, metadata, "APP", "CALENDAR_HOLIDAYS");
    }

}

