package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SAccount is a Querydsl query type for SAccount
 */
@Table("ACCOUNT")
public class SAccount extends BeanPath<SAccount> implements RelationalPath<SAccount> {

    private static final long serialVersionUID = -727563068;

    public static final SAccount account = new SAccount("ACCOUNT");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> ownerI = createNumber("OWNER_I", Long.class);

    public final PString somedata = createString("SOMEDATA");

    private Expr[] _all;

    public final PrimaryKey<SAccount> sql100819184429820 = new PrimaryKey<SAccount>(this, id);

    public final ForeignKey<SPerson> fk1d0c220d257b5f1c = new ForeignKey<SPerson>(this, ownerI, "I");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable));
    }

    public SAccount(BeanPath<? extends SAccount> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, ownerI, somedata};
        }
        return _all;
    }

    public PrimaryKey<SAccount> getPrimaryKey() {
        return sql100819184429820;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk1d0c220d257b5f1c);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

}

