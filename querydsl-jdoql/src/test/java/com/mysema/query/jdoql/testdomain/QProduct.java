/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.path.*;

/**
 * QProduct is a Querydsl query type for Product
 *
 */
@SuppressWarnings("serial")
public class QProduct extends PEntity<com.mysema.query.jdoql.testdomain.Product>{
    
    public static final QProduct product = new QProduct("product");
    
    public final PString description = createString("description");
    
    public final PString name = createString("name");
    
    public final PDateTime<java.util.Date> publicationDate = createDateTime("publicationDate",java.util.Date.class);
    
    public final PNumber<java.lang.Integer> amount = createNumber("amount",java.lang.Integer.class);
    
    public final PNumber<java.lang.Double> price = createNumber("price",java.lang.Double.class);
        
    public QProduct(java.lang.String path) {
          this(com.mysema.query.jdoql.testdomain.Product.class, path);        
    }
    public QProduct(Class<? extends com.mysema.query.jdoql.testdomain.Product> cl, java.lang.String path) {
          super(cl, "Product", path);
    }    
    public QProduct(PathMetadata<?> metadata) {
         super(com.mysema.query.jdoql.testdomain.Product.class, "Product", metadata);
    }
}
