package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SKittensSet is a Querydsl query type for SKittensSet
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SKittensSet extends com.mysema.query.sql.RelationalPathBase<SKittensSet> {

    private static final long serialVersionUID = -227477337;

    public static final SKittensSet kittensSet = new SKittensSet("KITTENS_SET");

    public final NumberPath<Integer> animal_id = createNumber("ANIMAL__ID", Integer.class);

    public final NumberPath<Integer> kittenssetId = createNumber("KITTENSSET_ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SKittensSet> sql120219232325810 = createPrimaryKey(animal_id, kittenssetId);

    public final com.mysema.query.sql.ForeignKey<SAnimal> fk4fccad6f913d64b8 = createForeignKey(kittenssetId, "ID");

    public final com.mysema.query.sql.ForeignKey<SAnimal> fk4fccad6ff2c6f4cb = createForeignKey(animal_id, "ID");

    public SKittensSet(String variable) {
        super(SKittensSet.class, forVariable(variable), "APP", "KITTENS_SET");
    }

    public SKittensSet(Path<? extends SKittensSet> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "KITTENS_SET");
    }

    public SKittensSet(PathMetadata<?> metadata) {
        super(SKittensSet.class, metadata, "APP", "KITTENS_SET");
    }

}

