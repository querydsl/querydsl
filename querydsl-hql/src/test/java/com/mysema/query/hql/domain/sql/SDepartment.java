package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SDepartment is a Querydsl query type for SDepartment
 */
@Table("DEPARTMENT")
public class SDepartment extends BeanPath<SDepartment> implements RelationalPath<SDepartment> {

    private static final long serialVersionUID = -774771365;

    public static final SDepartment department = new SDepartment("DEPARTMENT");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    private Expression[] _all;

    public final PrimaryKey<SDepartment> sql100819184432690 = new PrimaryKey<SDepartment>(this, id);

    public final ForeignKey<SCompany> fka9601f72555fdbf0 = new ForeignKey<SCompany>(this, companyId, "ID");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable));
    }

    public SDepartment(BeanPath<? extends SDepartment> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{companyId, id, name};
        }
        return _all;
    }

    public PrimaryKey<SDepartment> getPrimaryKey() {
        return sql100819184432690;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fka9601f72555fdbf0);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

