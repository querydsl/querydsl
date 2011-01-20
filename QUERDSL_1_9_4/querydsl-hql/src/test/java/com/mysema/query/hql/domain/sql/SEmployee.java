package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SEmployee is a Querydsl query type for SEmployee
 */
@Table("EMPLOYEE")
public class SEmployee extends BeanPath<SEmployee> implements RelationalPath<SEmployee> {

    private static final long serialVersionUID = -2043969801;

    public static final SEmployee employee = new SEmployee("EMPLOYEE");

    public final PNumber<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final PString firstname = createString("FIRSTNAME");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString lastname = createString("LASTNAME");

    private Expr[] _all;

    public final PrimaryKey<SEmployee> sql100819184433200 = new PrimaryKey<SEmployee>(this, id);

    public final ForeignKey<SCompany> fk4afd4ace555fdbf0 = new ForeignKey<SCompany>(this, companyId, "ID");

    public final ForeignKey<SCompany> _fk9bdfd45d8e79ac65 = new ForeignKey<SCompany>(this, id, "CEO_ID");

    public SEmployee(String variable) {
        super(SEmployee.class, forVariable(variable));
    }

    public SEmployee(BeanPath<? extends SEmployee> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SEmployee(PathMetadata<?> metadata) {
        super(SEmployee.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{companyId, firstname, id, lastname};
        }
        return _all;
    }

    public PrimaryKey<SEmployee> getPrimaryKey() {
        return sql100819184433200;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk4afd4ace555fdbf0);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk9bdfd45d8e79ac65);
    }
    
    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }

}

