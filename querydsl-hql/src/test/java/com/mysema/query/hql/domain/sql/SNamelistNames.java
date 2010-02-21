package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SNamelistNames is a Querydsl query type for SNamelistNames
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="NAMELIST_NAMES")
public class SNamelistNames extends PEntity<SNamelistNames> {

    public final PString element = createString("ELEMENT");

    public final PNumber<Long> namelistId = createNumber("NAMELIST_ID", Long.class);

    public SNamelistNames(String variable) {
        super(SNamelistNames.class, forVariable(variable));
    }

    public SNamelistNames(PEntity<? extends SNamelistNames> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SNamelistNames(PathMetadata<?> metadata) {
        super(SNamelistNames.class, metadata);
    }

}

