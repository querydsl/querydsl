package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SAnimal is a Querydsl query type for SAnimal
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SAnimal extends com.mysema.query.sql.RelationalPathBase<SAnimal> {

    private static final long serialVersionUID = 335015469;

    public static final SAnimal animal = new SAnimal("animal_");

    public final BooleanPath alive = createBoolean("alive");

    public final DateTimePath<java.sql.Timestamp> birthdate = createDateTime("birthdate", java.sql.Timestamp.class);

    public final NumberPath<Double> bodyWeight = createNumber("bodyWeight", Double.class);

    public final NumberPath<Integer> breed = createNumber("breed", Integer.class);

    public final NumberPath<Integer> color = createNumber("color", Integer.class);

    public final DatePath<java.sql.Date> dateField = createDate("dateField", java.sql.Date.class);

    public final StringPath dtype = createString("DTYPE");

    public final NumberPath<Integer> eyecolor = createNumber("eyecolor", Integer.class);

    public final NumberPath<Float> floatProperty = createNumber("floatProperty", Float.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> mateId = createNumber("mate_id", Integer.class);

    public final StringPath name = createString("name");

    public final TimePath<java.sql.Time> timeField = createTime("timeField", java.sql.Time.class);

    public final NumberPath<Integer> toes = createNumber("toes", Integer.class);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SAnimal> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkccec31e312a37469 = createForeignKey(mateId, "id");

    public final com.mysema.query.sql.ForeignKey<SKittensSet> _fk4fccad6f913d64b8 = createInvForeignKey(id, "kittensSet_id");

    public final com.mysema.query.sql.ForeignKey<SAnimal> _fkccec31e312a37469 = createInvForeignKey(id, "mate_id");

    public final com.mysema.query.sql.ForeignKey<SKittens> _fkd60087ccf2c6f4cb = createInvForeignKey(id, "animal__id");

    public final com.mysema.query.sql.ForeignKey<SKittens> _fkd60087cc88406a42 = createInvForeignKey(id, "kittens_id");

    public final com.mysema.query.sql.ForeignKey<SKittensSet> _fk4fccad6ff2c6f4cb = createInvForeignKey(id, "animal__id");

    public SAnimal(String variable) {
        super(SAnimal.class, forVariable(variable), "null", "animal_");
    }

    public SAnimal(Path<? extends SAnimal> path) {
        super(path.getType(), path.getMetadata(), "null", "animal_");
    }

    public SAnimal(PathMetadata<?> metadata) {
        super(SAnimal.class, metadata, "null", "animal_");
    }

}

