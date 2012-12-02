package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCalendarHOLIDAYS is a Querydsl query type for SCalendarHOLIDAYS
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCalendarHOLIDAYS2 extends com.mysema.query.sql.RelationalPathBase<SCalendarHOLIDAYS2> {

    private static final long serialVersionUID = 1568491338;

    public static final SCalendarHOLIDAYS2 CalendarHOLIDAYS = new SCalendarHOLIDAYS2("Calendar_HOLIDAYS");

    public final NumberPath<Integer> calendarID = createNumber("Calendar_ID", Integer.class);

    public final DatePath<java.sql.Date> holidays = createDate("HOLIDAYS", java.sql.Date.class);

    public final StringPath holidaysKey = createString("HOLIDAYS_KEY");

    public final com.mysema.query.sql.ForeignKey<SCalendar> calendarHOLIDAYSCalendarIDFK = createForeignKey(calendarID, "id");

    public SCalendarHOLIDAYS2(String variable) {
        super(SCalendarHOLIDAYS2.class, forVariable(variable), "null", "Calendar_HOLIDAYS");
    }

    public SCalendarHOLIDAYS2(Path<? extends SCalendarHOLIDAYS2> path) {
        super(path.getType(), path.getMetadata(), "null", "Calendar_HOLIDAYS");
    }

    public SCalendarHOLIDAYS2(PathMetadata<?> metadata) {
        super(SCalendarHOLIDAYS2.class, metadata, "null", "Calendar_HOLIDAYS");
    }

}

