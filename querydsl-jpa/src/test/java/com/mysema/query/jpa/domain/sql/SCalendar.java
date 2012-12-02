package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCalendar is a Querydsl query type for SCalendar
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCalendar extends com.mysema.query.sql.RelationalPathBase<SCalendar> {

    private static final long serialVersionUID = 444207919;

    public static final SCalendar calendar = new SCalendar("calendar_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SCalendar> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SNationality> _nationality_CALENDARIDFK = createInvForeignKey(id, "CALENDAR_ID");

    public final com.mysema.query.sql.ForeignKey<SCalendarHolidays> _fk31ce1edc591ebbc = createInvForeignKey(id, "Calendar_id");

    public final com.mysema.query.sql.ForeignKey<SCalendarHOLIDAYS2> _calendarHOLIDAYSCalendarIDFK = createInvForeignKey(id, "Calendar_ID");

    public SCalendar(String variable) {
        super(SCalendar.class, forVariable(variable), "null", "calendar_");
    }

    public SCalendar(Path<? extends SCalendar> path) {
        super(path.getType(), path.getMetadata(), "null", "calendar_");
    }

    public SCalendar(PathMetadata<?> metadata) {
        super(SCalendar.class, metadata, "null", "calendar_");
    }

}

