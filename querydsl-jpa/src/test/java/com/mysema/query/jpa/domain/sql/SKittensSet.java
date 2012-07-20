package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SKittensSet is a Querydsl query type for SKittensSet
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SKittensSet extends com.mysema.query.sql.RelationalPathBase<SKittensSet> {

    private static final long serialVersionUID = -227477337;

    public static final SKittensSet kittensSet = new SKittensSet("kittens_set");

    public final NumberPath<Integer> animal_id = createNumber("animal__id", Integer.class);

    public final NumberPath<Integer> kittensSetId = createNumber("kittensSet_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SKittensSet> primary = createPrimaryKey(animal_id, kittensSetId);

    public final com.mysema.query.sql.ForeignKey<SAnimal> fk4fccad6f913d64b8 = createForeignKey(kittensSetId, "id");

    public final com.mysema.query.sql.ForeignKey<SAnimal> fk4fccad6ff2c6f4cb = createForeignKey(animal_id, "id");

    public SKittensSet(String variable) {
        super(SKittensSet.class, forVariable(variable), "null", "kittens_set");
    }

    public SKittensSet(Path<? extends SKittensSet> path) {
        super(path.getType(), path.getMetadata(), "null", "kittens_set");
    }

    public SKittensSet(PathMetadata<?> metadata) {
        super(SKittensSet.class, metadata, "null", "kittens_set");
    }

}

