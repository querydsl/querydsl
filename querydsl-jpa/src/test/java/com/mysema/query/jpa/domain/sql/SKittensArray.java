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
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;

/**
 * SKittensArray is a Querydsl query type for SKittensArray
 */
@SuppressWarnings("serial")
public class SKittensArray extends RelationalPathBase<SKittensArray> {

    public final NumberPath<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final NumberPath<Integer> arrayindex = createNumber("ARRAYINDEX", Integer.class);

    public final NumberPath<Integer> kittensarrayId = createNumber("KITTENSARRAY_ID", Integer.class);

    public SKittensArray(String variable) {
        super(SKittensArray.class, forVariable(variable), null, "KITTENS_ARRAY");
    }

    public SKittensArray(BeanPath<? extends SKittensArray> entity) {
        super(entity.getType(),entity.getMetadata(), null, "KITTENS_ARRAY");
    }

    public SKittensArray(PathMetadata<?> metadata) {
        super(SKittensArray.class, metadata, null, "KITTENS_ARRAY");
    }

}

