package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

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

}

