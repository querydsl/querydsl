package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SShowActs is a Querydsl query type for SShowActs
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SHOW_ACTS")
public class SShowActs extends PEntity<SShowActs> {

    public final PString element = createString("ELEMENT");

    public final PString mapkey = createString("MAPKEY");

    public final PNumber<Integer> showId = createNumber("SHOW_ID", Integer.class);

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable));
    }

    public SShowActs(PEntity<? extends SShowActs> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata);
    }

}

