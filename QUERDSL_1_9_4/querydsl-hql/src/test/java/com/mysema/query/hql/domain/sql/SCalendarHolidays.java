package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SCalendarHolidays is a Querydsl query type for SCalendarHolidays
 */
@Table("CALENDAR_HOLIDAYS")
public class SCalendarHolidays extends BeanPath<SCalendarHolidays> implements RelationalPath<SCalendarHolidays> {

    private static final long serialVersionUID = 1051026370;

    public static final SCalendarHolidays calendarHolidays = new SCalendarHolidays("CALENDAR_HOLIDAYS");

    public final PNumber<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final PDateTime<Date> element = createDateTime("ELEMENT", Date.class);

    public final PString holidaysKey = createString("HOLIDAYS_KEY");

    private Expr[] _all;

    public final PrimaryKey<SCalendarHolidays> sql100819184431240 = new PrimaryKey<SCalendarHolidays>(this, calendarId, holidaysKey);

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

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{calendarId, element, holidaysKey};
        }
        return _all;
    }

    public PrimaryKey<SCalendarHolidays> getPrimaryKey() {
        return sql100819184431240;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk31ce1edca61b9464);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

