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
import com.mysema.query.types.Expression;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.SimpleTemplate;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.TimePath;


/**
 * SProduct is a Querydsl query type for SProduct
 */
@Table(value="PRODUCT")
public class SProduct extends BeanPath<SProduct> implements RelationalPath<SProduct>{

    private static final long serialVersionUID = -590374403;

    public static final SProduct product = new SProduct("PRODUCT");

    public final NumberPath<Integer> amount = createNumber("AMOUNT", Integer.class);

    public final DatePath<java.sql.Date> datefield = createDate("DATEFIELD", java.sql.Date.class);

    public final StringPath description = createString("DESCRIPTION");

    public final StringPath name = createString("NAME");

    public final NumberPath<Double> price = createNumber("PRICE", Double.class);

    public final NumberPath<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final DateTimePath<java.util.Date> publicationdate = createDateTime("PUBLICATIONDATE", java.util.Date.class);

    public final TimePath<java.sql.Time> timefield = createTime("TIMEFIELD", java.sql.Time.class);

    public final PrimaryKey<SProduct> sysIdx47 = new PrimaryKey<SProduct>(this, productId);

    public final ForeignKey<SStoreProducts> _storeProductsFk2 = new ForeignKey<SStoreProducts>(this, productId, "PRODUCT_ID_EID");

    public final ForeignKey<SBook> _bookFk1 = new ForeignKey<SBook>(this, productId, "BOOK_ID");

    public final ForeignKey<SStoreProductsbyname> _storeProductsbynameFk2 = new ForeignKey<SStoreProductsbyname>(this, productId, "PRODUCT_ID_VID");

    public SProduct(String variable) {
        super(SProduct.class, forVariable(variable));
    }

    public SProduct(BeanPath<? extends SProduct> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SProduct(PathMetadata<?> metadata) {
        super(SProduct.class, metadata);
    }

    public Expression<Object[]> all() {
        return SimpleTemplate.create(Object[].class, "{0}.*", this);
    }

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_storeProductsFk2, _bookFk1, _storeProductsbynameFk2);
    }

    @Override
    public PrimaryKey<SProduct> getPrimaryKey() {
        return sysIdx47;
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(amount, datefield, description, name, price, productId, publicationdate, timefield);
    }

}

