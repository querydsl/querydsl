package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SNationality is a Querydsl query type for SNationality
 */
@Table("NATIONALITY")
public class SNationality extends BeanPath<SNationality> implements RelationalPath<SNationality> {

    private static final long serialVersionUID = 1320834259;

    public static final SNationality nationality = new SNationality("NATIONALITY");

    public final PNumber<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final PNumber<Long> id = createNumber("ID", Long.class);

    private Expr[] _all;

    public final PrimaryKey<SNationality> sql100819184436080 = new PrimaryKey<SNationality>(this, id);

    public final ForeignKey<SCalendar> fk68f2659ca61b9464 = new ForeignKey<SCalendar>(this, calendarId, "ID");

    public final ForeignKey<SPerson> _fk8e488775e9d94490 = new ForeignKey<SPerson>(this, id, "NATIONALITY_ID");

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable));
    }

    public SNationality(BeanPath<? extends SNationality> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{calendarId, id};
        }
        return _all;
    }

    public PrimaryKey<SNationality> getPrimaryKey() {
        return sql100819184436080;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk68f2659ca61b9464);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk8e488775e9d94490);
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

