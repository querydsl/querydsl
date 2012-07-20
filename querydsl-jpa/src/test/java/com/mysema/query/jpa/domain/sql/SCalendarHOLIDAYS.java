package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCalendarHOLIDAYS is a Querydsl query type for SCalendarHOLIDAYS
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCalendarHOLIDAYS extends com.mysema.query.sql.RelationalPathBase<SCalendarHOLIDAYS> {

    private static final long serialVersionUID = 1568491338;

    public static final SCalendarHOLIDAYS CalendarHOLIDAYS = new SCalendarHOLIDAYS("Calendar_HOLIDAYS");

    public final NumberPath<Integer> calendarID = createNumber("Calendar_ID", Integer.class);

    public final DatePath<java.sql.Date> holidays = createDate("HOLIDAYS", java.sql.Date.class);

    public final StringPath holidaysKey = createString("HOLIDAYS_KEY");

    public final com.mysema.query.sql.ForeignKey<SCalendar> calendarHOLIDAYSCalendarIDFK = createForeignKey(calendarID, "id");

    public SCalendarHOLIDAYS(String variable) {
        super(SCalendarHOLIDAYS.class, forVariable(variable), "null", "Calendar_HOLIDAYS");
    }

    public SCalendarHOLIDAYS(Path<? extends SCalendarHOLIDAYS> path) {
        super(path.getType(), path.getMetadata(), "null", "Calendar_HOLIDAYS");
    }

    public SCalendarHOLIDAYS(PathMetadata<?> metadata) {
        super(SCalendarHOLIDAYS.class, metadata, "null", "Calendar_HOLIDAYS");
    }

}

