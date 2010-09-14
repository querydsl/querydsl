/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo.test.domain;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.CollectionPath;
import com.mysema.query.types.path.MapPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * QStore is a Querydsl query type for Store
 *
 */
@SuppressWarnings("serial")
public class QStore extends EntityPathBase<com.mysema.query.jdo.test.domain.Store>{

    public static final QStore store = new QStore("store");

    public final StringPath name = createString("name");

    public final MapPath<String,Product,QProduct> productsByName = this.<String,Product,QProduct>createMap("productsByName",String.class,Product.class,QProduct.class);

    public final CollectionPath<Product> products = createCollection("products",Product.class);

    public QProduct productsByName(String key) {
        return new QProduct(PathMetadataFactory.forMapAccess(productsByName,key));
    }

    public QProduct productsByName(com.mysema.query.types.Expression<String> key) {
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
