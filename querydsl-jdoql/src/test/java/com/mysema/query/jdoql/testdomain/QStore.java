/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.testdomain;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * QStore is a Querydsl query type for Store
 *
 */
@SuppressWarnings("serial")
public class QStore extends PEntity<com.mysema.query.jdoql.testdomain.Store>{

    public static final QStore store = new QStore("store");

    public final PString name = createString("name");

    public final PMap<String,Product,QProduct> productsByName = this.<String,Product,QProduct>createMap("productsByName",String.class,Product.class,QProduct.class);

    public final PCollection<Product> products = createCollection("products",Product.class);

    public QProduct productsByName(String key) {
        return new QProduct(PathMetadataFactory.forMapAccess(productsByName,key));
    }

    public QProduct productsByName(com.mysema.query.types.Expr<String> key) {
        return new QProduct(PathMetadataFactory.forMapAccess(productsByName,key));
    }

    public QStore(String path) {
          this(Store.class, path);
    }
    public QStore(Class<? extends Store> cl, String path) {
          super(cl, PathMetadataFactory.forVariable(path));
    }
    public QStore(PathMetadata<?> metadata) {
         super(Store.class, metadata);
    }
}
