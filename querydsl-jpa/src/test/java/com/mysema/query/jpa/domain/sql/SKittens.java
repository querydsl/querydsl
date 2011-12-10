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

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SKittens is a Querydsl query type for SKittens
 */
public class SKittens extends RelationalPathBase<SKittens> {

    private static final long serialVersionUID = -254852509;

    public static final SKittens kittens = new SKittens("KITTENS");

    public final NumberPath<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final NumberPath<Integer> ind = createNumber("IND", Integer.class);

    public final NumberPath<Integer> kittensId = createNumber("KITTENS_ID", Integer.class);

    public final PrimaryKey<SKittens> sql100819184440200 = createPrimaryKey(animalId, ind);

    public final ForeignKey<SAnimal> fkd60087cca295046a = new ForeignKey<SAnimal>(this, animalId, "ID");

    public final ForeignKey<SAnimal> fkd60087cc7a9f89a = new ForeignKey<SAnimal>(this, kittensId, "ID");

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable), null, "KITTENS");
    }

    public SKittens(BeanPath<? extends SKittens> entity) {
        super(entity.getType(), entity.getMetadata(), null, "KITTENS");
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata, null, "KITTENS");
    }

}

