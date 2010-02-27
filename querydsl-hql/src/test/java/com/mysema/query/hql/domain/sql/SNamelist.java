package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadata;

/**
 * SNamelist is a Querydsl query type for SNamelist
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="NAMELIST")
public class SNamelist extends PEntity<SNamelist> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public SNamelist(String variable) {
        super(SNamelist.class, forVariable(variable));
    }

    public SNamelist(PEntity<? extends SNamelist> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SNamelist(PathMetadata<?> metadata) {
        super(SNamelist.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

