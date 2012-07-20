package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCatalog_price is a Querydsl query type for SCatalog_price
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCatalog_price extends com.mysema.query.sql.RelationalPathBase<SCatalog_price> {

    private static final long serialVersionUID = 1703572562;

    public static final SCatalog_price catalog_price = new SCatalog_price("catalog__price_");

    public final NumberPath<Integer> catalog_id = createNumber("catalog__id", Integer.class);

    public final NumberPath<Integer> catalogID = createNumber("Catalog_ID", Integer.class);

    public final NumberPath<Long> pricesID = createNumber("prices_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCatalog_price> primary = createPrimaryKey(catalogID, pricesID);

    public final com.mysema.query.sql.ForeignKey<SCatalog> fkaa04532fbb9021ab = createForeignKey(catalog_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SPrice> catalog_price_pricesIDFK = createForeignKey(pricesID, "ID");

    public final com.mysema.query.sql.ForeignKey<SCatalog> catalog_price_CatalogIDFK = createForeignKey(catalogID, "ID");

    public SCatalog_price(String variable) {
        super(SCatalog_price.class, forVariable(variable), "null", "catalog__price_");
    }

    public SCatalog_price(Path<? extends SCatalog_price> path) {
        super(path.getType(), path.getMetadata(), "null", "catalog__price_");
    }

    public SCatalog_price(PathMetadata<?> metadata) {
        super(SCatalog_price.class, metadata, "null", "catalog__price_");
    }

}

