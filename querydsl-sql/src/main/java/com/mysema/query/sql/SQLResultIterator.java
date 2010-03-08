/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.QueryException;

/**
 * SQLResultIterator is an Iterator adapter for JDBC result sets with customizable projections
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class SQLResultIterator<T> implements CloseableIterator<T> {

    @Nullable
    private Boolean next = null;

    private final ResultSet rs;
    
    private final Statement stmt;
    
    public SQLResultIterator(Statement stmt, ResultSet rs){
        this.stmt = stmt;
        this.rs = rs;
    }
    
    @Override
    public void close(){
        try{
            try {
                if (rs != null){
                    rs.close();    
                }                
            } finally {
                if (stmt != null){
                    stmt.close();    
                }                
            }    
        }catch(SQLException e){
            throw new QueryException(e);
        }                    
    }
    
    @Override
    public boolean hasNext() {
        if (next == null){
            try {
                next = rs.next();
            } catch (SQLException e) {
                close();
                throw new QueryException(e);
            }
        }
        return next;
    }
    
    @Override
    public T next() {
        if (hasNext()){
            next = null;
            return produceNext(rs);
        }else{
            throw new NoSuchElementException();
        }
    }

    protected abstract T produceNext(ResultSet rs);
    
    @Override
    public void remove() {
        try {
            rs.deleteRow();
        } catch (SQLException e) {
            close();
            throw new QueryException(e);
        }
    }

}
