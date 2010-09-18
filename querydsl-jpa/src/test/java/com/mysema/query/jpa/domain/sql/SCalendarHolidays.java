package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import java.util.Date;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SCalendarHolidays is a Querydsl query type for SCalendarHolidays
 */
@Table("CALENDAR_HOLIDAYS")
public class SCalendarHolidays extends RelationalPathBase<SCalendarHolidays> {

    private static final long serialVersionUID = 1051026370;

    public static final SCalendarHolidays calendarHolidays = new SCalendarHolidays("CALENDAR_HOLIDAYS");

    public final NumberPath<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final DateTimePath<Date> element = createDateTime("ELEMENT", Date.class);

    public final StringPath holidaysKey = createString("HOLIDAYS_KEY");

    public final PrimaryKey<SCalendarHolidays> sql100819184431240 = createPrimaryKey(calendarId, holidaysKey);

    public final ForeignKey<SCalendar> fk31ce1edca61b9464 = new ForeignKey<SCalendar>(this, calendarId, "ID");

    public SCalendarHolidays(String variable) {
        super(SCalendarHolidays.class, forVariable(variable));
    }

    public SCalendarHolidays(BeanPath<? extends SCalendarHolidays> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SCalendarHolidays(PathMetadata<?> metadata) {
        super(SCalendarHolidays.class, metadata);
    }

}

