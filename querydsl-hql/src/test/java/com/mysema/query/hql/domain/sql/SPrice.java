package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SPrice is a Querydsl query type for SPrice
 */
@Table("PRICE")
public class SPrice extends BeanPath<SPrice> implements RelationalPath<SPrice> {

    private static final long serialVersionUID = -1644550752;

    public static final SPrice price = new SPrice("PRICE");

    public final PNumber<Long> amount = createNumber("AMOUNT", Long.class);

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> productId = createNumber("PRODUCT_ID", Long.class);

    private Expr[] _all;

    public final PrimaryKey<SPrice> sql100819184437800 = new PrimaryKey<SPrice>(this, id);

    public final ForeignKey<SItem> fk49cc129a549aeb0 = new ForeignKey<SItem>(this, productId, "ID");

    public final ForeignKey<SCatalogPrice> _fke4eb7d639d62434f = new ForeignKey<SCatalogPrice>(this, id, "PRICES_ID");

    public SPrice(String variable) {
        super(SPrice.class, forVariable(variable));
    }

    public SPrice(BeanPath<? extends SPrice> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SPrice(PathMetadata<?> metadata) {
        super(SPrice.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{amount, id, productId};
        }
        return _all;
    }

    public PrimaryKey<SPrice> getPrimaryKey() {
        return sql100819184437800;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk49cc129a549aeb0);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fke4eb7d639d62434f);
    }

}

