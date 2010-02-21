package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SLocation is a Querydsl query type for SLocation
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="LOCATION")
public class SLocation extends PEntity<SLocation> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString name = createString("NAME");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable));
    }

    public SLocation(PEntity<? extends SLocation> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

