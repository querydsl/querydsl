package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SAnimal is a Querydsl query type for SAnimal
 */
@Table("ANIMAL")
public class SAnimal extends BeanPath<SAnimal> implements RelationalPath<SAnimal> {

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

    private Expression[] _all;

    public final PrimaryKey<SAnimal> sql100819184430090 = new PrimaryKey<SAnimal>(this, id);

    public final ForeignKey<SAnimal> fk752a7a1c920d02c1 = new ForeignKey<SAnimal>(this, mateId, "ID");

    public final ForeignKey<SKittens> _fkd60087cca295046a = new ForeignKey<SKittens>(this, id, "ANIMAL_ID");

    public final ForeignKey<SKittensSet> _fk4fccad6fa295046a = new ForeignKey<SKittensSet>(this, id, "ANIMAL_ID");

    public final ForeignKey<SKittensSet> _fk4fccad6f10a6f310 = new ForeignKey<SKittensSet>(this, id, "KITTENSSET_ID");

    public final ForeignKey<SAnimal> _fk752a7a1c920d02c1 = new ForeignKey<SAnimal>(this, id, "MATE_ID");

    public final ForeignKey<SKittens> _fkd60087cc7a9f89a = new ForeignKey<SKittens>(this, id, "KITTENS_ID");

    public SAnimal(String variable) {
        super(SAnimal.class, forVariable(variable));
    }

    public SAnimal(BeanPath<? extends SAnimal> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SAnimal(PathMetadata<?> metadata) {
        super(SAnimal.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{alive, birthdate, bodyweight, breed, color, datefield, dtype, eyecolor, id, mateId, name, timefield, toes, weight};
        }
        return _all;
    }

    public PrimaryKey<SAnimal> getPrimaryKey() {
        return sql100819184430090;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk752a7a1c920d02c1);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fkd60087cca295046a, _fk4fccad6fa295046a, _fk4fccad6f10a6f310, _fk752a7a1c920d02c1, _fkd60087cc7a9f89a);
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

