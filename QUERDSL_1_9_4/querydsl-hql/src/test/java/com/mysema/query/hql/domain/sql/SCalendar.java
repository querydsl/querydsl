package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SCalendar is a Querydsl query type for SCalendar
 */
@Table("CALENDAR")
public class SCalendar extends BeanPath<SCalendar> implements RelationalPath<SCalendar> {

    private static final long serialVersionUID = 879203207;

    public static final SCalendar calendar = new SCalendar("CALENDAR");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    private Expr[] _all;

    public final PrimaryKey<SCalendar> sql100819184430980 = new PrimaryKey<SCalendar>(this, id);

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

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id};
        }
        return _all;
    }

    public PrimaryKey<SCalendar> getPrimaryKey() {
        return sql100819184430980;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk68f2659ca61b9464, _fk31ce1edca61b9464);
    }
    
    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }

}

