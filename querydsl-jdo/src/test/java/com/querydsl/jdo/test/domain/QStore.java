/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.jdo.test.domain;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.MapPath;
import com.querydsl.core.types.path.PathInits;
import com.querydsl.core.types.path.StringPath;

/**
 * QStore is a Querydsl querydsl type for Store
 *
 */
@SuppressWarnings("serial")
public class QStore extends EntityPathBase<com.querydsl.jdo.test.domain.Store>{

    public static final QStore store = new QStore("store");

    public final StringPath name = createString("name");

    public final MapPath<String, Product, QProduct> productsByName = this.<String, Product, QProduct>createMap("productsByName",String.class,Product.class,QProduct.class);

    public final ListPath<Product, QProduct> products = this.<Product, QProduct>createList("products",Product.class,QProduct.class, PathInits.DIRECT);

    public QProduct productsByName(String key) {
        return new QProduct(PathMetadataFactory.forMapAccess(productsByName,key));
    }

    public QProduct productsByName(com.querydsl.core.types.Expression<String> key) {
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
