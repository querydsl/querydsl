package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPrice is a Querydsl query type for SPrice
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPrice extends com.mysema.query.sql.RelationalPathBase<SPrice> {

    private static final long serialVersionUID = -113768712;

    public static final SPrice price = new SPrice("price_");

    public final NumberPath<Long> amount = createNumber("AMOUNT", Long.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SPrice> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem> price_PRODUCTIDFK = createForeignKey(productId, "ID");

    public final com.mysema.query.sql.ForeignKey<SCatalog_price> _catalog_price_pricesIDFK = createInvForeignKey(id, "prices_ID");

    public SPrice(String variable) {
        super(SPrice.class, forVariable(variable), "null", "price_");
    }

    public SPrice(Path<? extends SPrice> path) {
        super(path.getType(), path.getMetadata(), "null", "price_");
    }

    public SPrice(PathMetadata<?> metadata) {
        super(SPrice.class, metadata, "null", "price_");
    }

}

