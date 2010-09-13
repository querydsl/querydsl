package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SKittens is a Querydsl query type for SKittens
 */
@Table("KITTENS")
public class SKittens extends BeanPath<SKittens> implements RelationalPath<SKittens> {

    private static final long serialVersionUID = -254852509;

    public static final SKittens kittens = new SKittens("KITTENS");

    public final NumberPath<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final NumberPath<Integer> ind = createNumber("IND", Integer.class);

    public final NumberPath<Integer> kittensId = createNumber("KITTENS_ID", Integer.class);

    private Expression[] _all;

    public final PrimaryKey<SKittens> sql100819184440200 = new PrimaryKey<SKittens>(this, animalId, ind);

    public final ForeignKey<SAnimal> fkd60087cca295046a = new ForeignKey<SAnimal>(this, animalId, "ID");

    public final ForeignKey<SAnimal> fkd60087cc7a9f89a = new ForeignKey<SAnimal>(this, kittensId, "ID");

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable));
    }

    public SKittens(BeanPath<? extends SKittens> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{animalId, ind, kittensId};
        }
        return _all;
    }

    public PrimaryKey<SKittens> getPrimaryKey() {
        return sql100819184440200;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fkd60087cca295046a, fkd60087cc7a9f89a);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

