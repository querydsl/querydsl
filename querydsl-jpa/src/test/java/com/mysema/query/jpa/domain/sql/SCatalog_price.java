package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCatalog_price is a Querydsl query type for SCatalog_price
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCatalog_price extends com.mysema.query.sql.RelationalPathBase<SCatalog_price> {

    private static final long serialVersionUID = 1703572562;

    public static final SCatalog_price catalog_price = new SCatalog_price("CATALOG__PRICE_");

    public final NumberPath<Integer> catalog_id = createNumber("CATALOG__ID", Integer.class);

    public final NumberPath<Long> pricesId = createNumber("PRICES_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCatalog_price> sql120219232320970 = createPrimaryKey(catalog_id, pricesId);

    public final com.mysema.query.sql.ForeignKey<SCatalog> fkaa04532fbb9021ab = createForeignKey(catalog_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SPrice> fkaa04532f5222eaf7 = createForeignKey(pricesId, "ID");

    public SCatalog_price(String variable) {
        super(SCatalog_price.class, forVariable(variable), "APP", "CATALOG__PRICE_");
    }

    public SCatalog_price(Path<? extends SCatalog_price> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CATALOG__PRICE_");
    }

    public SCatalog_price(PathMetadata<?> metadata) {
        super(SCatalog_price.class, metadata, "APP", "CATALOG__PRICE_");
    }

}

