package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SPrice is a Querydsl query type for SPrice
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PRICE")
public class SPrice extends PEntity<SPrice> {

    public final PNumber<Long> amount = createNumber("AMOUNT", Long.class);

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public SPrice(String variable) {
        super(SPrice.class, forVariable(variable));
    }

    public SPrice(PEntity<? extends SPrice> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPrice(PathMetadata<?> metadata) {
        super(SPrice.class, metadata);
    }

}

