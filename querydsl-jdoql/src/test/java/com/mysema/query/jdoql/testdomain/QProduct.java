/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.TimePath;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * QProduct is a Querydsl query type for Product
 *
 */
@SuppressWarnings("serial")
public class QProduct extends EntityPathBase<com.mysema.query.jdoql.testdomain.Product>{

    public static final QProduct product = new QProduct("product");

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final DateTimePath<java.util.Date> publicationDate = createDateTime("publicationDate",java.util.Date.class);

    public final DatePath<java.sql.Date> dateField = createDate("dateField", java.sql.Date.class);

    public final TimePath<java.sql.Time> timeField = createTime("timeField", java.sql.Time.class);

    public final NumberPath<Integer> amount = createNumber("amount",Integer.class);

    public final NumberPath<Double> price = createNumber("price",Double.class);

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
