/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.util;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import com.mysema.query.types.Param;
import com.mysema.query.types.ParamNotSetException;

/**
 * @author tiwe
 *
 */
public final class JDBCUtil {

    private JDBCUtil(){}

    public static void setParameters(PreparedStatement stmt, Collection<Object> objects, Map<Param<?>, Object> params){
        int counter = 1;
        for (Object o : objects) {
            try {
                if (Param.class.isInstance(o)){
                    if (!params.containsKey(o)){
                        throw new ParamNotSetException((Param<?>) o);
                    }
                    o = params.get(o);
                }
                setParameter(stmt, counter++, o);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private static void setParameter(PreparedStatement stmt, int i, Object o) throws SQLException{
        Class<?> type = o.getClass();        
        if (type.equals(BigDecimal.class)){
            stmt.setBigDecimal(i, (BigDecimal) o);    
        }else if (type.equals(Blob.class)){
            stmt.setBlob(i, (Blob) o);    
        }else if (type.equals(Boolean.class)){
            stmt.setBoolean(i, (Boolean) o);    
        }else if (type.equals(Byte.class)){
            stmt.setByte(i, (Byte)o);    
        }else if (type.equals(byte[].class)){
            stmt.setBytes(i, (byte[])o); 
        }else if (type.equals(Character.class)){
            stmt.setString(i, String.valueOf(o.toString()));            
        }else if (type.equals(Clob.class)){
            stmt.setClob(i, (Clob)o);    
        }else if (type.equals(java.sql.Date.class)){
            stmt.setDate(i, (java.sql.Date)o);    
        }else if (type.equals(java.util.Date.class)){
            stmt.setDate(i, new java.sql.Date(((java.util.Date)o).getTime()));
        }else if (type.equals(Double.class)){
            stmt.setDouble(i, (Double) o);    
        }else if (type.equals(Float.class)){
            stmt.setFloat(i, (Float) o);    
        }else if (type.equals(Integer.class)){
            stmt.setInt(i, (Integer) o);    
        }else if (type.equals(Long.class)){
            stmt.setLong(i, (Long) o);    
        }else if (type.equals(Short.class)){
            stmt.setShort(i, (Short) o);    
        }else if (type.equals(String.class)){
            stmt.setString(i, (String)o);    
        }else if (type.equals(Time.class)){
            stmt.setTime(i, (Time)o);    
        }else if (type.equals(Timestamp.class)){
            stmt.setTimestamp(i, (Timestamp)o);    
        }else if (type.equals(URL.class)){
            stmt.setURL(i, (URL)o);    
        }else{
            stmt.setObject(i, o);
        }
    }

}
