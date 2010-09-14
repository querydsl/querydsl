package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SCalendar is a Querydsl query type for SCalendar
 */
@Table("CALENDAR")
public class SCalendar extends RelationalPathBase<SCalendar> {

    private static final long serialVersionUID = 879203207;

    public static final SCalendar calendar = new SCalendar("CALENDAR");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final PrimaryKey<SCalendar> sql100819184430980 = createPrimaryKey(id);

    public final ForeignKey<SNationality> _fk68f2659ca61b9464 = new ForeignKey<SNationality>(this, id, "CALENDAR_ID");

    public final ForeignKey<SCalendarHolidays> _fk31ce1edca61b9464 = new ForeignKey<SCalendarHolidays>(this, id, "CALENDAR_ID");

    public SCalendar(String variable) {
        super(SCalendar.class, forVariable(variable));
    }

    public SCalendar(BeanPath<? extends SCalendar> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SCalendar(PathMetadata<?> metadata) {
        super(SCalendar.class, metadata);
    }

}

