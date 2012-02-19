package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPrice is a Querydsl query type for SPrice
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SPrice extends com.mysema.query.sql.RelationalPathBase<SPrice> {

    private static final long serialVersionUID = -113768712;

    public static final SPrice price = new SPrice("PRICE_");

    public final NumberPath<Long> amount = createNumber("AMOUNT", Long.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SPrice> sql120219232328930 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem> fkc59678362c7f0c58 = createForeignKey(productId, "ID");

    public final com.mysema.query.sql.ForeignKey<SCatalog_price> _fkaa04532f5222eaf7 = createInvForeignKey(id, "PRICES_ID");

    public SPrice(String variable) {
        super(SPrice.class, forVariable(variable), "APP", "PRICE_");
    }

    public SPrice(Path<? extends SPrice> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "PRICE_");
    }

    public SPrice(PathMetadata<?> metadata) {
        super(SPrice.class, metadata, "APP", "PRICE_");
    }

}

