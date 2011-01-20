/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.sql.types.BigDecimalType;
import com.mysema.query.sql.types.BooleanType;
import com.mysema.query.sql.types.ByteType;
import com.mysema.query.sql.types.BytesType;
import com.mysema.query.sql.types.CharacterType;
import com.mysema.query.sql.types.DateType;
import com.mysema.query.sql.types.DoubleType;
import com.mysema.query.sql.types.FloatType;
import com.mysema.query.sql.types.IntegerType;
import com.mysema.query.sql.types.LongType;
import com.mysema.query.sql.types.ObjectType;
import com.mysema.query.sql.types.ShortType;
import com.mysema.query.sql.types.StringType;
import com.mysema.query.sql.types.TimeType;
import com.mysema.query.sql.types.TimestampType;
import com.mysema.query.sql.types.Type;
import com.mysema.query.sql.types.URLType;
import com.mysema.query.sql.types.UtilDateType;

public class TypeTest implements InvocationHandler{
    
    private Object value;    

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args.length == 2){
            value = args[1];
            return null;
        }else{
            return value;
        }
    }
    
    private ResultSet resultSet = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{ResultSet.class}, this);
    
    private PreparedStatement statement = (PreparedStatement) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{PreparedStatement.class}, this);
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() throws MalformedURLException, SQLException{
        List<Pair> valueAndType = new ArrayList<Pair>();
        valueAndType.add(Pair.of(new BigDecimal("1"), new BigDecimalType()));
        valueAndType.add(Pair.of(new Boolean(true),   new BooleanType()));
        valueAndType.add(Pair.of(new Byte((byte)1),   new ByteType()));
        valueAndType.add(Pair.of(new byte[0],         new BytesType()));        
        valueAndType.add(Pair.of(new Character('c'),  new CharacterType()));
        valueAndType.add(Pair.of(new java.sql.Date(0),new DateType()));
        valueAndType.add(Pair.of(new Double(1),       new DoubleType()));
        valueAndType.add(Pair.of(new Float(1),        new FloatType()));
        valueAndType.add(Pair.of(new Integer(1),      new IntegerType()));
        valueAndType.add(Pair.of(new Long(1),         new LongType()));
        valueAndType.add(Pair.of(new Object(),        new ObjectType()));
        valueAndType.add(Pair.of(new Short((short)1), new ShortType()));
        valueAndType.add(Pair.of(new String(""),      new StringType()));
        valueAndType.add(Pair.of(new Timestamp(0),    new TimestampType()));
        valueAndType.add(Pair.of(new Time(0),         new TimeType()));
        valueAndType.add(Pair.of(new URL("http://www.mysema.com"), new URLType()));
        valueAndType.add(Pair.of(new java.util.Date(),new UtilDateType()));
        
        for (Pair pair : valueAndType){
            value = null;
            Type type = (Type) pair.getSecond();
            assertNull(type.getValue(resultSet, 0));
            type.setValue(statement, 0, pair.getFirst());
            assertEquals(pair.getFirst(), type.getValue(resultSet, 0));
        }
    }


}
