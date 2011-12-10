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

import java.util.Date;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.TimePath;


/**
 * SAnimal is a Querydsl query type for SAnimal
 */
public class SAnimal extends RelationalPathBase<SAnimal> {

    private static final long serialVersionUID = 125412485;

    public static final SAnimal animal = new SAnimal("ANIMAL");

    public final NumberPath<Short> alive = createNumber("ALIVE", Short.class);

    public final DateTimePath<Date> birthdate = createDateTime("BIRTHDATE", Date.class);

    public final NumberPath<Double> bodyweight = createNumber("BODYWEIGHT", Double.class);

    public final NumberPath<Integer> breed = createNumber("BREED", Integer.class);

    public final NumberPath<Integer> color = createNumber("COLOR", Integer.class);

    public final DatePath<java.sql.Date> datefield = createDate("DATEFIELD", java.sql.Date.class);

    public final StringPath dtype = createString("DTYPE");

    public final NumberPath<Integer> eyecolor = createNumber("EYECOLOR", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Integer> mateId = createNumber("MATE_ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final TimePath<java.sql.Time> timefield = createTime("TIMEFIELD", java.sql.Time.class);

    public final NumberPath<Integer> toes = createNumber("TOES", Integer.class);

    public final NumberPath<Integer> weight = createNumber("WEIGHT", Integer.class);

    public final PrimaryKey<SAnimal> sql100819184430090 = createPrimaryKey(id);

    public final ForeignKey<SAnimal> fk752a7a1c920d02c1 = new ForeignKey<SAnimal>(this, mateId, "ID");

    public final ForeignKey<SKittens> _fkd60087cca295046a = new ForeignKey<SKittens>(this, id, "ANIMAL_ID");

    public final ForeignKey<SKittensSet> _fk4fccad6fa295046a = new ForeignKey<SKittensSet>(this, id, "ANIMAL_ID");

    public final ForeignKey<SKittensSet> _fk4fccad6f10a6f310 = new ForeignKey<SKittensSet>(this, id, "KITTENSSET_ID");

    public final ForeignKey<SAnimal> _fk752a7a1c920d02c1 = new ForeignKey<SAnimal>(this, id, "MATE_ID");

    public final ForeignKey<SKittens> _fkd60087cc7a9f89a = new ForeignKey<SKittens>(this, id, "KITTENS_ID");

    public SAnimal(String variable) {
        super(SAnimal.class, forVariable(variable), null, "ANIMAL");
    }

    public SAnimal(BeanPath<? extends SAnimal> entity) {
        super(entity.getType(), entity.getMetadata(), null, "ANIMAL");
    }

    public SAnimal(PathMetadata<?> metadata) {
        super(SAnimal.class, metadata, null, "ANIMAL");
    }

}

