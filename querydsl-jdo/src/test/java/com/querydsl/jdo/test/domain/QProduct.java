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
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.DateTimePath;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.core.types.path.TimePath;

/**
 * QProduct is a Querydsl querydsl type for Product
 *
 */
@SuppressWarnings("serial")
public class QProduct extends EntityPathBase<com.querydsl.jdo.test.domain.Product>{

    public static final QProduct product = new QProduct("product");

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final DateTimePath<java.util.Date> publicationDate = createDateTime("publicationDate",java.util.Date.class);

    public final DatePath<java.sql.Date> dateField = createDate("dateField", java.sql.Date.class);

    public final TimePath<java.sql.Time> timeField = createTime("timeField", java.sql.Time.class);

    public final NumberPath<Integer> amount = createNumber("amount",Integer.class);

    public final NumberPath<Double> price = createNumber("price",Double.class);

    public QProduct(String path) {
          this(com.querydsl.jdo.test.domain.Product.class, path);
    }
    
    public QProduct(Class<? extends com.querydsl.jdo.test.domain.Product> cl, String path) {
          super(cl, PathMetadataFactory.forVariable(path));
    }
    
    public QProduct(PathMetadata<?> metadata) {
         super(com.querydsl.jdo.test.domain.Product.class, metadata);
    }
    
}
