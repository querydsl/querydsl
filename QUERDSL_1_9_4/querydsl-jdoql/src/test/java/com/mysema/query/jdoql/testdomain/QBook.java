/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * QBook is a Querydsl query type for Book
 *
 */
@SuppressWarnings("serial")
public class QBook extends EntityPathBase<com.mysema.query.jdoql.testdomain.Book>{

    public static final QBook book = new QBook("book");

    public final PString author = createString("author");

    public final PString description = createString("description");

    public final PString isbn = createString("isbn");

    public final PString name = createString("name");

    public final PString publisher = createString("publisher");

    public final PDateTime<java.util.Date> publicationDate = createDateTime("publicationDate",java.util.Date.class);

    public final PNumber<Integer> amount = createNumber("amount",Integer.class);

    public final PNumber<Double> price = createNumber("price",Double.class);

    public QBook(String path) {
          this(com.mysema.query.jdoql.testdomain.Book.class, path);
    }
    public QBook(Class<? extends com.mysema.query.jdoql.testdomain.Book> cl, String path) {
          super(cl, PathMetadataFactory.forVariable(path));
    }
    public QBook(PathMetadata<?> metadata) {
         super(com.mysema.query.jdoql.testdomain.Book.class, metadata);
    }
}
