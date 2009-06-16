package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.path.*;

/**
 * QBook is a Querydsl query type for Book
 *
 */
public class QBook extends PEntity<com.mysema.query.jdoql.testdomain.Book>{
    public static final QBook book = new QBook("book");
    public final PString author = _string("author");
    public final PString description = _string("description");
    public final PString isbn = _string("isbn");
    public final PString name = _string("name");
    public final PString publisher = _string("publisher");
    public final PDateTime<java.util.Date> publicationDate = _dateTime("publicationDate",java.util.Date.class);
    public final PNumber<java.lang.Integer> amount = _number("amount",java.lang.Integer.class);
    public final PNumber<java.lang.Double> price = _number("price",java.lang.Double.class);
    
    public QBook(java.lang.String path) {
          this(com.mysema.query.jdoql.testdomain.Book.class, path);        
    }
    public QBook(Class<? extends com.mysema.query.jdoql.testdomain.Book> cl, java.lang.String path) {
          super(cl, "Book", path);
    }    
    public QBook(PathMetadata<?> metadata) {
         super(com.mysema.query.jdoql.testdomain.Book.class, "Book", metadata);
    }
}
