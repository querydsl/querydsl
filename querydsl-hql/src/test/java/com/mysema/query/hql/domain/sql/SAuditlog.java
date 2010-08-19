package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SAuditlog is a Querydsl query type for SAuditlog
 */
@Table("AUDITLOG")
public class SAuditlog extends BeanPath<SAuditlog> implements RelationalPath<SAuditlog> {

    private static final long serialVersionUID = 2033602002;

    public static final SAuditlog auditlog = new SAuditlog("AUDITLOG");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Long> itemId = createNumber("ITEM_ID", Long.class);

    private Expr[] _all;

    public final PrimaryKey<SAuditlog> sql100819184430350 = new PrimaryKey<SAuditlog>(this, id);

    public final ForeignKey<SItem> fk3e07a1891bee4d44 = new ForeignKey<SItem>(this, itemId, "ID");

    public SAuditlog(String variable) {
        super(SAuditlog.class, forVariable(variable));
    }

    public SAuditlog(BeanPath<? extends SAuditlog> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SAuditlog(PathMetadata<?> metadata) {
        super(SAuditlog.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, itemId};
        }
        return _all;
    }

    public PrimaryKey<SAuditlog> getPrimaryKey() {
        return sql100819184430350;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk3e07a1891bee4d44);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

}

