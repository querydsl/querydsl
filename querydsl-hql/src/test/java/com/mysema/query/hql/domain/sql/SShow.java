package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SShow is a Querydsl query type for SShow
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SHOW")
public class SShow extends PEntity<SShow> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public SShow(String variable) {
        super(SShow.class, forVariable(variable));
    }

    public SShow(PEntity<? extends SShow> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

