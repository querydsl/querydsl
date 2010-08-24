package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SLocation is a Querydsl query type for SLocation
 */
@Table("LOCATION")
public class SLocation extends BeanPath<SLocation> implements RelationalPath<SLocation> {

    private static final long serialVersionUID = -1336395778;

    public static final SLocation location = new SLocation("LOCATION");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString name = createString("NAME");

    private Expr[] _all;

    public final PrimaryKey<SLocation> sql100819184434890 = new PrimaryKey<SLocation>(this, id);

    public final ForeignKey<SStore> _fk4c808c12adf2d04 = new ForeignKey<SStore>(this, id, "LOCATION_ID");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable));
    }

    public SLocation(BeanPath<? extends SLocation> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, name};
        }
        return _all;
    }

    public PrimaryKey<SLocation> getPrimaryKey() {
        return sql100819184434890;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk4c808c12adf2d04);
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

