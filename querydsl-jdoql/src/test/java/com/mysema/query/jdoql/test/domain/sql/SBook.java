package com.mysema.query.jdoql.test.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.Table;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;


/**
 * SBook is a Querydsl query type for SBook
 */
@Table(value="BOOK")
public class SBook extends PEntity<SBook> {

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

    public SBook(PEntity<? extends SBook> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SBook(PathMetadata<?> metadata) {
        super(SBook.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

