package com.querydsl.r2dbc.types;

import java.sql.Types;

/**
 * NullType
 */
public class NullType extends AbstractType<Null, Null> {

    public NullType() {
        super(Types.NULL);
    }

    public NullType(int type) {
        super(type);
    }

    @Override
    public Class<Null> getReturnedClass() {
        return Null.class;
    }

}
