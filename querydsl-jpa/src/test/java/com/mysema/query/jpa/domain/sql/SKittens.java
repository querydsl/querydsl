package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SKittens is a Querydsl query type for SKittens
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SKittens extends com.mysema.query.sql.RelationalPathBase<SKittens> {

    private static final long serialVersionUID = 1947872699;

    public static final SKittens kittens = new SKittens("kittens");

    public final NumberPath<Integer> animal_id = createNumber("animal__id", Integer.class);

    public final NumberPath<Integer> ind = createNumber("ind", Integer.class);

    public final NumberPath<Integer> kittensId = createNumber("kittens_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SKittens> primary = createPrimaryKey(animal_id, ind);

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkd60087ccf2c6f4cb = createForeignKey(animal_id, "id");

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkd60087cc88406a42 = createForeignKey(kittensId, "id");

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable), "null", "kittens");
    }

    public SKittens(Path<? extends SKittens> path) {
        super(path.getType(), path.getMetadata(), "null", "kittens");
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata, "null", "kittens");
    }

}

