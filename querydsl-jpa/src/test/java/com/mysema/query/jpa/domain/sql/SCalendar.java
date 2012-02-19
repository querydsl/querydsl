package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCalendar is a Querydsl query type for SCalendar
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCalendar extends com.mysema.query.sql.RelationalPathBase<SCalendar> {

    private static final long serialVersionUID = 444207919;

    public static final SCalendar calendar = new SCalendar("CALENDAR_");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SCalendar> sql120219232320550 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCalendarHolidays> _fk31ce1edc591ebbc = createInvForeignKey(id, "CALENDAR_ID");

    public final com.mysema.query.sql.ForeignKey<SNationality> _fkab8efa23591ebbc = createInvForeignKey(id, "CALENDAR_ID");

    public SCalendar(String variable) {
        super(SCalendar.class, forVariable(variable), "APP", "CALENDAR_");
    }

    public SCalendar(Path<? extends SCalendar> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CALENDAR_");
    }

    public SCalendar(PathMetadata<?> metadata) {
        super(SCalendar.class, metadata, "APP", "CALENDAR_");
    }

}

