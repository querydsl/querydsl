package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SCatalogPrice is a Querydsl query type for SCatalogPrice
 */
@Table("CATALOG_PRICE")
public class SCatalogPrice extends RelationalPathBase<SCatalogPrice> {

    private static final long serialVersionUID = -1748773767;

    public static final SCatalogPrice catalogPrice = new SCatalogPrice("CATALOG_PRICE");

    public final NumberPath<Integer> catalogId = createNumber("CATALOG_ID", Integer.class);

    public final NumberPath<Long> pricesId = createNumber("PRICES_ID", Long.class);

    public final PrimaryKey<SCatalogPrice> sql100819184431880 = createPrimaryKey(catalogId, pricesId);

    public final ForeignKey<SPrice> fke4eb7d639d62434f = new ForeignKey<SPrice>(this, pricesId, "ID");

    public final ForeignKey<SCatalog> fke4eb7d63f28fe670 = new ForeignKey<SCatalog>(this, catalogId, "ID");

    public SCatalogPrice(String variable) {
        super(SCatalogPrice.class, forVariable(variable));
    }

    public SCatalogPrice(BeanPath<? extends SCatalogPrice> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SCatalogPrice(PathMetadata<?> metadata) {
        super(SCatalogPrice.class, metadata);
    }

}

