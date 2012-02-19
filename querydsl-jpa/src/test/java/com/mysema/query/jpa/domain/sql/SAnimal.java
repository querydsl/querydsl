package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SAnimal is a Querydsl query type for SAnimal
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SAnimal extends com.mysema.query.sql.RelationalPathBase<SAnimal> {

    private static final long serialVersionUID = 335015469;

    public static final SAnimal animal = new SAnimal("ANIMAL_");

    public final NumberPath<Short> alive = createNumber("ALIVE", Short.class);

    public final DateTimePath<java.sql.Timestamp> birthdate = createDateTime("BIRTHDATE", java.sql.Timestamp.class);

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

    public final com.mysema.query.sql.PrimaryKey<SAnimal> sql120219232319450 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkccec31e312a37469 = createForeignKey(mateId, "ID");

    public final com.mysema.query.sql.ForeignKey<SKittensSet> _fk4fccad6f913d64b8 = createInvForeignKey(id, "KITTENSSET_ID");

    public final com.mysema.query.sql.ForeignKey<SAnimal> _fkccec31e312a37469 = createInvForeignKey(id, "MATE_ID");

    public final com.mysema.query.sql.ForeignKey<SKittens> _fkd60087ccf2c6f4cb = createInvForeignKey(id, "ANIMAL__ID");

    public final com.mysema.query.sql.ForeignKey<SKittens> _fkd60087cc88406a42 = createInvForeignKey(id, "KITTENS_ID");

    public final com.mysema.query.sql.ForeignKey<SKittensSet> _fk4fccad6ff2c6f4cb = createInvForeignKey(id, "ANIMAL__ID");

    public SAnimal(String variable) {
        super(SAnimal.class, forVariable(variable), "APP", "ANIMAL_");
    }

    public SAnimal(Path<? extends SAnimal> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "ANIMAL_");
    }

    public SAnimal(PathMetadata<?> metadata) {
        super(SAnimal.class, metadata, "APP", "ANIMAL_");
    }

}

