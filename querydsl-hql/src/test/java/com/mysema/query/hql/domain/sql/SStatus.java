package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SStatus is a Querydsl query type for SStatus
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="STATUS")
public class SStatus extends PEntity<SStatus> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString name = createString("NAME");

    public SStatus(String variable) {
        super(SStatus.class, forVariable(variable));
    }

    public SStatus(PEntity<? extends SStatus> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStatus(PathMetadata<?> metadata) {
        super(SStatus.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

