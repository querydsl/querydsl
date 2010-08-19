/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * QProduct is a Querydsl query type for Product
 *
 */
@SuppressWarnings("serial")
public class QProduct extends EntityPathBase<com.mysema.query.jdoql.testdomain.Product>{

    public static final QProduct product = new QProduct("product");

    public final PString description = createString("description");

    public final PString name = createString("name");

    public final PDateTime<java.util.Date> publicationDate = createDateTime("publicationDate",java.util.Date.class);

    public final PDate<java.sql.Date> dateField = createDate("dateField", java.sql.Date.class);

    public final PTime<java.sql.Time> timeField = createTime("timeField", java.sql.Time.class);

    public final PNumber<Integer> amount = createNumber("amount",Integer.class);

    public final PNumber<Double> price = createNumber("price",Double.class);

    public QProduct(String path) {
          this(com.mysema.query.jdoql.testdomain.Product.class, path);
    }
    public QProduct(Class<? extends com.mysema.query.jdoql.testdomain.Product> cl, String path) {
          super(cl, PathMetadataFactory.forVariable(path));
    }
    public QProduct(PathMetadata<?> metadata) {
         super(com.mysema.query.jdoql.testdomain.Product.class, metadata);
    }
}
