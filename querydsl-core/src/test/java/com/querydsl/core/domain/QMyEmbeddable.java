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
package com.querydsl.core.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;

import javax.annotation.Generated;


/**
 * QMyEmbeddable is a Querydsl querydsl type for MyEmbeddable
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QMyEmbeddable extends BeanPath<MyEmbeddable> {

    private static final long serialVersionUID = -968265626;

    public static final QMyEmbeddable myEmbeddable = new QMyEmbeddable("myEmbeddable");

    public final NumberPath<Integer> foo = createNumber("foo", Integer.class);

    public QMyEmbeddable(String variable) {
        super(MyEmbeddable.class, forVariable(variable));
    }

    public QMyEmbeddable(BeanPath<? extends MyEmbeddable> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QMyEmbeddable(PathMetadata<?> metadata) {
        super(MyEmbeddable.class, metadata);
    }

}
