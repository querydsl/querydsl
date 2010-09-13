package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SCatalogPrice is a Querydsl query type for SCatalogPrice
 */
@Table("CATALOG_PRICE")
public class SCatalogPrice extends BeanPath<SCatalogPrice> implements RelationalPath<SCatalogPrice> {

    private static final long serialVersionUID = -1748773767;

    public static final SCatalogPrice catalogPrice = new SCatalogPrice("CATALOG_PRICE");

    public final NumberPath<Integer> catalogId = createNumber("CATALOG_ID", Integer.class);

    public final NumberPath<Long> pricesId = createNumber("PRICES_ID", Long.class);

    private Expression[] _all;

    public final PrimaryKey<SCatalogPrice> sql100819184431880 = new PrimaryKey<SCatalogPrice>(this, catalogId, pricesId);

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

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{catalogId, pricesId};
        }
        return _all;
    }

    public PrimaryKey<SCatalogPrice> getPrimaryKey() {
        return sql100819184431880;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fke4eb7d639d62434f, fke4eb7d63f28fe670);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

