package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SCatalogPrice is a Querydsl query type for SCatalogPrice
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="CATALOG_PRICE")
public class SCatalogPrice extends PEntity<SCatalogPrice> {

    public final PNumber<Integer> catalogId = createNumber("CATALOG_ID", Integer.class);

    public final PNumber<Long> pricesId = createNumber("PRICES_ID", Long.class);

    public SCatalogPrice(String variable) {
        super(SCatalogPrice.class, forVariable(variable));
    }

    public SCatalogPrice(PEntity<? extends SCatalogPrice> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCatalogPrice(PathMetadata<?> metadata) {
        super(SCatalogPrice.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

