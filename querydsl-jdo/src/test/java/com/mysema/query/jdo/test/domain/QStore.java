/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.jdo.test.domain;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.ListPath;
import com.mysema.query.types.path.MapPath;
import com.mysema.query.types.path.PathInits;
import com.mysema.query.types.path.StringPath;

/**
 * QStore is a Querydsl query type for Store
 *
 */
@SuppressWarnings("serial")
public class QStore extends EntityPathBase<com.mysema.query.jdo.test.domain.Store>{

    public static final QStore store = new QStore("store");

    public final StringPath name = createString("name");

    public final MapPath<String, Product, QProduct> productsByName = this.<String, Product, QProduct>createMap("productsByName",String.class,Product.class,QProduct.class);

    public final ListPath<Product, QProduct> products = this.<Product, QProduct>createList("products",Product.class,QProduct.class, PathInits.DIRECT);

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
