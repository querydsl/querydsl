package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SFormula is a Querydsl query type for SFormula
 */
@Table("FORMULA")
public class SFormula extends BeanPath<SFormula> implements RelationalPath<SFormula> {

    private static final long serialVersionUID = -227668995;

    public static final SFormula formula = new SFormula("FORMULA");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Long> parameterId = createNumber("PARAMETER_ID", Long.class);

    private Expr[] _all;

    public final PrimaryKey<SFormula> sql100819184433810 = new PrimaryKey<SFormula>(this, id);

    public final ForeignKey<SParameter> fk3ad7e94694c3fef0 = new ForeignKey<SParameter>(this, parameterId, "ID");

    public SFormula(String variable) {
        super(SFormula.class, forVariable(variable));
    }

    public SFormula(BeanPath<? extends SFormula> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SFormula(PathMetadata<?> metadata) {
        super(SFormula.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, parameterId};
        }
        return _all;
    }

    public PrimaryKey<SFormula> getPrimaryKey() {
        return sql100819184433810;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk3ad7e94694c3fef0);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }
    
    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }

}

