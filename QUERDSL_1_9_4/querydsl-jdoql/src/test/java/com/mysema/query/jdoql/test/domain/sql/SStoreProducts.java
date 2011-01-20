package com.mysema.query.jdoql.test.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.Table;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.PNumber;


/**
 * SStoreProducts is a Querydsl query type for SStoreProducts
 */
@Table(value="STORE_PRODUCTS")
public class SStoreProducts extends BeanPath<SStoreProducts> implements RelationalPath<SStoreProducts>{

    private static final long serialVersionUID = 1019873267;

    public static final SStoreProducts storeProducts = new SStoreProducts("STORE_PRODUCTS");

    public final PNumber<Integer> idx = createNumber("IDX", Integer.class);

    public final PNumber<Long> productIdEid = createNumber("PRODUCT_ID_EID", Long.class);

    public final PNumber<Long> storeIdOid = createNumber("STORE_ID_OID", Long.class);

    public final PrimaryKey<SStoreProducts> sysIdx55 = new PrimaryKey<SStoreProducts>(this, idx, storeIdOid);

    public final ForeignKey<SProduct> storeProductsFk2 = new ForeignKey<SProduct>(this, productIdEid, "PRODUCT_ID");

    public final ForeignKey<SStore> storeProductsFk1 = new ForeignKey<SStore>(this, storeIdOid, "STORE_ID");

    public SStoreProducts(String variable) {
        super(SStoreProducts.class, forVariable(variable));
    }

    public SStoreProducts(BeanPath<? extends SStoreProducts> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStoreProducts(PathMetadata<?> metadata) {
        super(SStoreProducts.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(storeProductsFk2, storeProductsFk1);
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public PrimaryKey<SStoreProducts> getPrimaryKey() {
        return sysIdx55;
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(idx, productIdEid, storeIdOid);
    }

}

