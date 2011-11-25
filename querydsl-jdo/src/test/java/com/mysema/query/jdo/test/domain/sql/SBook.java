package com.mysema.query.jdo.test.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SBook is a Querydsl query type for SBook
 */
//@Table(value="BOOK")
public class SBook extends RelationalPathBase<SBook> {

    private static final long serialVersionUID = -1566558053;

    public static final SBook book = new SBook("BOOK");

    public final StringPath author = createString("AUTHOR");

    public final NumberPath<Long> bookId = createNumber("BOOK_ID", Long.class);

    public final StringPath isbn = createString("ISBN");

    public final StringPath publisher = createString("PUBLISHER");

    public final PrimaryKey<SBook> sysIdx65 = createPrimaryKey(bookId);

    public final ForeignKey<SProduct> bookFk1 = new ForeignKey<SProduct>(this, bookId, "PRODUCT_ID");

    public SBook(String variable) {
        super(SBook.class, forVariable(variable), null, "BOOK");
    }

    public SBook(BeanPath<? extends SBook> entity) {
        super(entity.getType(),entity.getMetadata(), null, "BOOK");
    }

    public SBook(PathMetadata<?> metadata) {
        super(SBook.class, metadata, null, "BOOK");
    }

}

