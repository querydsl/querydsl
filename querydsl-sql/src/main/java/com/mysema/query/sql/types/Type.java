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

    /**
     * @return
     */
    int[] getSQLTypes();

    /**
     * @return
     */
    Class<T> getReturnedClass();

    /**
     * @param rs
     * @param startIndex
     * @return
     * @throws SQLException
     */
    @Nullable
    T getValue(ResultSet rs, int startIndex) throws SQLException;

    /**
     * @param st
     * @param startIndex
     * @param value
     * @throws SQLException
     */
    void setValue(PreparedStatement st, int startIndex, T value) throws SQLException;

}