package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SParameter is a Querydsl query type for SParameter
 */
@Table("PARAMETER")
public class SParameter extends BeanPath<SParameter> implements RelationalPath<SParameter> {

    private static final long serialVersionUID = 378086528;

    public static final SParameter parameter = new SParameter("PARAMETER");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    private Expression[] _all;

    public final PrimaryKey<SParameter> sql100819184436610 = new PrimaryKey<SParameter>(this, id);

    public final ForeignKey<SFormula> _fk3ad7e94694c3fef0 = new ForeignKey<SFormula>(this, id, "PARAMETER_ID");

    public SParameter(String variable) {
        super(SParameter.class, forVariable(variable));
    }

    public SParameter(BeanPath<? extends SParameter> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SParameter(PathMetadata<?> metadata) {
        super(SParameter.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{id};
        }
        return _all;
    }

    public PrimaryKey<SParameter> getPrimaryKey() {
        return sql100819184436610;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk3ad7e94694c3fef0);
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

