package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SAnimal is a Querydsl querydsl type for SAnimal
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SAnimal extends com.querydsl.sql.RelationalPathBase<SAnimal> {

    private static final long serialVersionUID = 1795545042;

    public static final SAnimal animal_ = new SAnimal("animal_");

    public final BooleanPath alive = createBoolean("alive");

    public final DateTimePath<java.sql.Timestamp> birthdate = createDateTime("birthdate", java.sql.Timestamp.class);

    public final NumberPath<Double> bodyWeight = createNumber("bodyWeight", Double.class);

    public final NumberPath<Integer> breed = createNumber("breed", Integer.class);

    public final NumberPath<Integer> color = createNumber("color", Integer.class);

    public final DatePath<java.sql.Date> dateField = createDate("dateField", java.sql.Date.class);

    public final StringPath dtype = createString("dtype");

    public final NumberPath<Integer> eyecolor = createNumber("eyecolor", Integer.class);

    public final NumberPath<Float> floatProperty = createNumber("floatProperty", Float.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> mateId = createNumber("mateId", Integer.class);

    public final StringPath name = createString("name");

    public final TimePath<java.sql.Time> timeField = createTime("timeField", java.sql.Time.class);

    public final NumberPath<Integer> toes = createNumber("toes", Integer.class);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SAnimal> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SAnimal> fkccec31e312a37469 = createForeignKey(mateId, "id");

    public final com.querydsl.sql.ForeignKey<SKittens> _fkd60087cc3881aaa7 = createInvForeignKey(id, "kitten_id");

    public final com.querydsl.sql.ForeignKey<SAnimal> _fkccec31e312a37469 = createInvForeignKey(id, "mate_id");

    public final com.querydsl.sql.ForeignKey<SKittensSet> _fk4fccad6f8f00fdf8 = createInvForeignKey(id, "cat_id");

    public final com.querydsl.sql.ForeignKey<SKittens> _fkd60087cc8f00fdf8 = createInvForeignKey(id, "cat_id");

    public final com.querydsl.sql.ForeignKey<SKittensSet> _fk4fccad6f3881aaa7 = createInvForeignKey(id, "kitten_id");

    public SAnimal(String variable) {
        super(SAnimal.class, forVariable(variable), "", "animal_");
        addMetadata();
    }

    public SAnimal(String variable, String schema, String table) {
        super(SAnimal.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SAnimal(Path<? extends SAnimal> path) {
        super(path.getType(), path.getMetadata(), "", "animal_");
        addMetadata();
    }

    public SAnimal(PathMetadata<?> metadata) {
        super(SAnimal.class, metadata, "", "animal_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(alive, ColumnMetadata.named("alive").withIndex(3).ofType(-7).notNull());
        addMetadata(birthdate, ColumnMetadata.named("birthdate").withIndex(4).ofType(93).withSize(19));
        addMetadata(bodyWeight, ColumnMetadata.named("bodyWeight").withIndex(5).ofType(8).withSize(22).notNull());
        addMetadata(breed, ColumnMetadata.named("breed").withIndex(13).ofType(4).withSize(10));
        addMetadata(color, ColumnMetadata.named("color").withIndex(6).ofType(4).withSize(10));
        addMetadata(dateField, ColumnMetadata.named("dateField").withIndex(7).ofType(91).withSize(10));
        addMetadata(dtype, ColumnMetadata.named("DTYPE").withIndex(1).ofType(12).withSize(31).notNull());
        addMetadata(eyecolor, ColumnMetadata.named("eyecolor").withIndex(14).ofType(4).withSize(10));
        addMetadata(floatProperty, ColumnMetadata.named("floatProperty").withIndex(8).ofType(7).withSize(12).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(2).ofType(4).withSize(10).notNull());
        addMetadata(mateId, ColumnMetadata.named("mate_id").withIndex(15).ofType(4).withSize(10));
        addMetadata(name, ColumnMetadata.named("name").withIndex(9).ofType(12).withSize(255));
        addMetadata(timeField, ColumnMetadata.named("timeField").withIndex(10).ofType(92).withSize(8));
        addMetadata(toes, ColumnMetadata.named("toes").withIndex(11).ofType(4).withSize(10).notNull());
        addMetadata(weight, ColumnMetadata.named("weight").withIndex(12).ofType(4).withSize(10).notNull());
    }

}

