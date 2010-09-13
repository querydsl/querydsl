package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SKittensSet is a Querydsl query type for SKittensSet
 */
@Table("KITTENS_SET")
public class SKittensSet extends BeanPath<SKittensSet> implements RelationalPath<SKittensSet> {

    private static final long serialVersionUID = 1191166719;

    public static final SKittensSet kittensSet = new SKittensSet("KITTENS_SET");

    public final NumberPath<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final NumberPath<Integer> kittenssetId = createNumber("KITTENSSET_ID", Integer.class);

    private Expression[] _all;

    public final PrimaryKey<SKittensSet> sql100819184440700 = new PrimaryKey<SKittensSet>(this, animalId, kittenssetId);

    public final ForeignKey<SAnimal> fk4fccad6f10a6f310 = new ForeignKey<SAnimal>(this, kittenssetId, "ID");

    public final ForeignKey<SAnimal> fk4fccad6fa295046a = new ForeignKey<SAnimal>(this, animalId, "ID");

    public SKittensSet(String variable) {
        super(SKittensSet.class, forVariable(variable));
    }

    public SKittensSet(BeanPath<? extends SKittensSet> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SKittensSet(PathMetadata<?> metadata) {
        super(SKittensSet.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{animalId, kittenssetId};
        }
        return _all;
    }

    public PrimaryKey<SKittensSet> getPrimaryKey() {
        return sql100819184440700;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk4fccad6f10a6f310, fk4fccad6fa295046a);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }
}

