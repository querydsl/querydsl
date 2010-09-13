/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.PathMetadataFactory;

@Table("PERSON")
public class QPerson extends BeanPath<QPerson> implements RelationalPath<QPerson> {

    private static final long serialVersionUID = 475064746;

    public static final QPerson person = new QPerson("PERSON");

    public final StringPath firstname = createString("FIRSTNAME");

    public final EnumPath<com.mysema.query.alias.AliasTest.Gender> gender = createEnum("GENDER", com.mysema.query.alias.AliasTest.Gender.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath securedid = createString("SECUREDID");

    private Expression<?>[] _all;

    public final PrimaryKey<QPerson> sysIdx118 = new PrimaryKey<QPerson>(this, id);

    public QPerson(String variable) {
        super(QPerson.class, PathMetadataFactory.forVariable(variable));
    }

    public QPerson(BeanPath<? extends QPerson> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPerson(PathMetadata<?> metadata) {
        super(QPerson.class, metadata);
    }

    public Expression<?>[] all() {
        if (_all == null) {
            _all = new Expression[]{firstname, gender, id, securedid};
        }
        return _all;
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.asList(all());
    }

    @Override
    public PrimaryKey<QPerson> getPrimaryKey() {
        return sysIdx118;
    }

    @Override
    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.emptyList();
    }

}