package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SKittens is a Querydsl query type for SKittens
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SKittens extends com.mysema.query.sql.RelationalPathBase<SKittens> {

    private static final long serialVersionUID = 1947872699;

    public static final SKittens kittens = new SKittens("KITTENS");

    public final NumberPath<Integer> animal_id = createNumber("ANIMAL__ID", Integer.class);

    public final NumberPath<Integer> ind = createNumber("IND", Integer.class);

    public final NumberPath<Integer> kittensId = createNumber("KITTENS_ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SKittens> sql120219232325460 = createPrimaryKey(animal_id, ind);

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkd60087ccf2c6f4cb = createForeignKey(animal_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkd60087cc88406a42 = createForeignKey(kittensId, "ID");

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable), "APP", "KITTENS");
    }

    public SKittens(Path<? extends SKittens> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "KITTENS");
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata, "APP", "KITTENS");
    }

}

