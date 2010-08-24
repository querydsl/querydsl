package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SCatalog is a Querydsl query type for SCatalog
 */
@Table("CATALOG")
public class SCatalog extends BeanPath<SCatalog> implements RelationalPath<SCatalog> {

    private static final long serialVersionUID = 1005460144;

    public static final SCatalog catalog = new SCatalog("CATALOG");

    public final PDate<java.sql.Date> effectivedate = createDate("EFFECTIVEDATE", java.sql.Date.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    private Expr[] _all;

    public final PrimaryKey<SCatalog> sql100819184431520 = new PrimaryKey<SCatalog>(this, id);

    public final ForeignKey<SCatalogPrice> _fke4eb7d63f28fe670 = new ForeignKey<SCatalogPrice>(this, id, "CATALOG_ID");

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable));
    }

    public SCatalog(BeanPath<? extends SCatalog> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{effectivedate, id};
        }
        return _all;
    }

    public PrimaryKey<SCatalog> getPrimaryKey() {
        return sql100819184431520;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fke4eb7d63f28fe670);
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

