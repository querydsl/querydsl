package com.mysema.query.jdoql.test.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.Table;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;


/**
 * SBook is a Querydsl query type for SBook
 */
@Table(value="BOOK")
public class SBook extends BeanPath<SBook> implements RelationalPath<SBook>{

    private static final long serialVersionUID = -1566558053;

    public static final SBook book = new SBook("BOOK");

    public final PString author = createString("AUTHOR");

    public final PNumber<Long> bookId = createNumber("BOOK_ID", Long.class);

    public final PString isbn = createString("ISBN");

    public final PString publisher = createString("PUBLISHER");

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

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
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

}

