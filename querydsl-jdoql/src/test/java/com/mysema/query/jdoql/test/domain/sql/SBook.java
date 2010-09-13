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
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SBook is a Querydsl query type for SBook
 */
@Table(value="BOOK")
public class SBook extends BeanPath<SBook> implements RelationalPath<SBook>{

    private static final long serialVersionUID = -1566558053;

    public static final SBook book = new SBook("BOOK");

    public final StringPath author = createString("AUTHOR");

    public final NumberPath<Long> bookId = createNumber("BOOK_ID", Long.class);

    public final StringPath isbn = createString("ISBN");

    public final StringPath publisher = createString("PUBLISHER");

    public final PrimaryKey<SBook> sysIdx65 = new PrimaryKey<SBook>(this, bookId);

    public final ForeignKey<SProduct> bookFk1 = new ForeignKey<SProduct>(this, bookId, "PRODUCT_ID");

    public SBook(String variable) {
        super(SBook.class, forVariable(variable));
    }

    public SBook(BeanPath<? extends SBook> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SBook(PathMetadata<?> metadata) {
        super(SBook.class, metadata);
    }

    public Expression<Object[]> all() {
        return SimpleTemplate.create(Object[].class, "{0}.*", this);
    }

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(bookFk1);
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public PrimaryKey<SBook> getPrimaryKey() {
        return sysIdx65;
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(author, bookId, isbn, publisher);
    }

}

