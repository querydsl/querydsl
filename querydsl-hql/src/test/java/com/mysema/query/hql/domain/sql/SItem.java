package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SItem is a Querydsl query type for SItem
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="ITEM")
public class SItem extends PEntity<SItem> {

    public final PNumber<Long> currentstatusId = createNumber("CURRENTSTATUS_ID", Long.class);

    public final PString dtype = createString("DTYPE");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Integer> name = createNumber("NAME", Integer.class);

    public final PNumber<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final PNumber<Long> statusId = createNumber("STATUS_ID", Long.class);

    public SItem(String variable) {
        super(SItem.class, forVariable(variable));
    }

    public SItem(PEntity<? extends SItem> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SItem(PathMetadata<?> metadata) {
        super(SItem.class, metadata);
    }

}

