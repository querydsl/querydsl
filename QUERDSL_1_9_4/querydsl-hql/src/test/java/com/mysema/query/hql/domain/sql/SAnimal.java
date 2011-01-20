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

    public final PNumber<Short> alive = createNumber("ALIVE", Short.class);

    public final PDateTime<Date> birthdate = createDateTime("BIRTHDATE", Date.class);

    public final PNumber<Double> bodyweight = createNumber("BODYWEIGHT", Double.class);

    public final PNumber<Integer> breed = createNumber("BREED", Integer.class);

    public final PNumber<Integer> color = createNumber("COLOR", Integer.class);

    public final PDate<java.sql.Date> datefield = createDate("DATEFIELD", java.sql.Date.class);

    public final PString dtype = createString("DTYPE");

    public final PNumber<Integer> eyecolor = createNumber("EYECOLOR", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Integer> mateId = createNumber("MATE_ID", Integer.class);

    public final PString name = createString("NAME");

    public final PTime<java.sql.Time> timefield = createTime("TIMEFIELD", java.sql.Time.class);

    public final PNumber<Integer> toes = createNumber("TOES", Integer.class);

    public final PNumber<Integer> weight = createNumber("WEIGHT", Integer.class);

    private Expr[] _all;

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

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{alive, birthdate, bodyweight, breed, color, datefield, dtype, eyecolor, id, mateId, name, timefield, toes, weight};
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
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }

}

