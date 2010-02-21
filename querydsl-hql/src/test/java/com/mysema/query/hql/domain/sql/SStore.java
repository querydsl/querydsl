package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SStore is a Querydsl query type for SStore
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="STORE")
public class SStore extends PEntity<SStore> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> locationId = createNumber("LOCATION_ID", Long.class);

    public SStore(String variable) {
        super(SStore.class, forVariable(variable));
    }

    public SStore(PEntity<? extends SStore> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata);
    }

}

