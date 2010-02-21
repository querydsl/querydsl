package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SNamed is a Querydsl query type for SNamed
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="NAMED")
public class SNamed extends PEntity<SNamed> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString name = createString("NAME");

    public SNamed(String variable) {
        super(SNamed.class, forVariable(variable));
    }

    public SNamed(PEntity<? extends SNamed> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SNamed(PathMetadata<?> metadata) {
        super(SNamed.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

