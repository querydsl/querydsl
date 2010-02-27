package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadata;

/**
 * SStatuschange is a Querydsl query type for SStatuschange
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="STATUSCHANGE")
public class SStatuschange extends PEntity<SStatuschange> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PComparable<java.util.Date> timestamp = createComparable("TIMESTAMP", java.util.Date.class);

    public SStatuschange(String variable) {
        super(SStatuschange.class, forVariable(variable));
    }

    public SStatuschange(PEntity<? extends SStatuschange> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStatuschange(PathMetadata<?> metadata) {
        super(SStatuschange.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

