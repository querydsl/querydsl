/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nullable;

/**
 * @author tiwe
 *
 * @param <T>
 */
public interface Type<T> {

    int[] getSQLTypes();

    Class<T> getReturnedClass();

    @Nullable
    T getValue(ResultSet rs, int startIndex) throws SQLException;

    void setValue(PreparedStatement st, int startIndex, T value) throws SQLException;

}