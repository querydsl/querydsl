package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SFoo is a Querydsl query type for SFoo
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="FOO")
public class SFoo extends PEntity<SFoo> {

    public final PString bar = createString("BAR");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PComparable<java.util.Date> startdate = createComparable("STARTDATE", java.util.Date.class);

    public SFoo(String variable) {
        super(SFoo.class, forVariable(variable));
    }

    public SFoo(PEntity<? extends SFoo> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SFoo(PathMetadata<?> metadata) {
        super(SFoo.class, metadata);
    }

}

