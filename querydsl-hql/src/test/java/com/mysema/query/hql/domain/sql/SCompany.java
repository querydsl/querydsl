package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SCompany is a Querydsl query type for SCompany
 */
@Table("COMPANY")
public class SCompany extends BeanPath<SCompany> implements RelationalPath<SCompany> {

    private static final long serialVersionUID = 1400239892;

    public static final SCompany company = new SCompany("COMPANY");

    public final PNumber<Integer> ceoId = createNumber("CEO_ID", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    private Expr[] _all;

    public final PrimaryKey<SCompany> sql100819184432220 = new PrimaryKey<SCompany>(this, id);

    public final ForeignKey<SEmployee> fk9bdfd45d8e79ac65 = new ForeignKey<SEmployee>(this, ceoId, "ID");

    public final ForeignKey<SDepartment> _fka9601f72555fdbf0 = new ForeignKey<SDepartment>(this, id, "COMPANY_ID");

    public final ForeignKey<SUser> _fk4d495f4555fdbf0 = new ForeignKey<SUser>(this, id, "COMPANY_ID");

    public final ForeignKey<SEmployee> _fk4afd4ace555fdbf0 = new ForeignKey<SEmployee>(this, id, "COMPANY_ID");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable));
    }

    public SCompany(BeanPath<? extends SCompany> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{ceoId, id, name};
        }
        return _all;
    }

    public PrimaryKey<SCompany> getPrimaryKey() {
        return sql100819184432220;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk9bdfd45d8e79ac65);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fka9601f72555fdbf0, _fk4d495f4555fdbf0, _fk4afd4ace555fdbf0);
    }

}

