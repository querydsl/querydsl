package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SUser is a Querydsl query type for SUser
 */
@Table("USER_")
public class SUser extends BeanPath<SUser> implements RelationalPath<SUser> {

    private static final long serialVersionUID = 501289108;

    public static final SUser user = new SUser("USER_");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath lastname = createString("LASTNAME");

    public final StringPath username = createString("USERNAME");

    private Expression[] _all;

    public final PrimaryKey<SUser> sql100819184439940 = new PrimaryKey<SUser>(this, id);

    public final ForeignKey<SCompany> fk4d495f4555fdbf0 = new ForeignKey<SCompany>(this, companyId, "ID");

    public SUser(String variable) {
        super(SUser.class, forVariable(variable));
    }

    public SUser(BeanPath<? extends SUser> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SUser(PathMetadata<?> metadata) {
        super(SUser.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{companyId, firstname, id, lastname, username};
        }
        return _all;
    }

    public PrimaryKey<SUser> getPrimaryKey() {
        return sql100819184439940;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk4d495f4555fdbf0);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }
}

