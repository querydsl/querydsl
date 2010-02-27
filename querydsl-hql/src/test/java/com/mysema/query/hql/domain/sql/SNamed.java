package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;

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

