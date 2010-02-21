package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SCatalog is a Querydsl query type for SCatalog
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="CATALOG")
public class SCatalog extends PEntity<SCatalog> {

    public final PComparable<java.util.Date> effectivedate = createComparable("EFFECTIVEDATE", java.util.Date.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable));
    }

    public SCatalog(PEntity<? extends SCatalog> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

