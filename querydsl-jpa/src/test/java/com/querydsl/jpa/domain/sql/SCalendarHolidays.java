package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCalendarHolidays is a Querydsl querydsl type for SCalendarHolidays
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCalendarHolidays extends com.querydsl.sql.RelationalPathBase<SCalendarHolidays> {

    private static final long serialVersionUID = 850508650;

    public static final SCalendarHolidays CalendarHolidays = new SCalendarHolidays("Calendar_holidays");

    public final NumberPath<Integer> calendarId = createNumber("calendarId", Integer.class);

    public final DatePath<java.sql.Date> holidays = createDate("holidays", java.sql.Date.class);

    public final StringPath holidaysKEY = createString("holidaysKEY");

    public final com.querydsl.sql.PrimaryKey<SCalendarHolidays> primary = createPrimaryKey(calendarId, holidaysKEY);

    public final com.querydsl.sql.ForeignKey<SCalendar> fk31ce1edc591ebbc = createForeignKey(calendarId, "id");

    public SCalendarHolidays(String variable) {
        super(SCalendarHolidays.class, forVariable(variable), "", "Calendar_holidays");
        addMetadata();
    }

    public SCalendarHolidays(String variable, String schema, String table) {
        super(SCalendarHolidays.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCalendarHolidays(Path<? extends SCalendarHolidays> path) {
        super(path.getType(), path.getMetadata(), "", "Calendar_holidays");
        addMetadata();
    }

    public SCalendarHolidays(PathMetadata<?> metadata) {
        super(SCalendarHolidays.class, metadata, "", "Calendar_holidays");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(calendarId, ColumnMetadata.named("Calendar_id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(holidays, ColumnMetadata.named("holidays").withIndex(2).ofType(91).withSize(10));
        addMetadata(holidaysKEY, ColumnMetadata.named("holidays_KEY").withIndex(3).ofType(12).withSize(255).notNull());
    }

}

