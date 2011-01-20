/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.extensions;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.path.PathMetadataFactory;

public class QueryExtensionsTest {

    @QueryEmbeddable
    public static class Point{

        @QueryMethod("geo_distance({0}, {1})")
        public int geoDistance(Point otherPoint){
            return 0;
        }

        @QueryMethod("{0}.getName()")
        public String getName(){
            return "";
        }

        @QueryMethod("{0}.getMap()")
        public Map<String,String> getMap(){
            return Collections.emptyMap();
        }

        @QueryMethod("{0}.getList()")
        public List<String> getList(){
            return Collections.emptyList();
        }

        @QueryMethod("{0}.getDate()")
        public Date getDate(){
            return null;
        }

        @QueryMethod("")
        public Point getPoint(){
            return null;
        }

        @QueryMethod("")
        public Point getPoint(Point point, SubPoint subPoint){
            return null;
        }

    }

    @QueryEmbeddable
    public static class SubPoint extends Point{

    }

    @Test
    public void test(){
        QQueryExtensionsTest_Point var = new QQueryExtensionsTest_Point(PathMetadataFactory.forVariable("var"));
        QQueryExtensionsTest_Point var2 = new QQueryExtensionsTest_Point(PathMetadataFactory.forVariable("var2"));

        // geoDistance
        assertEquals("geo_distance(var, var2)", var.geoDistance(var2).toString());

        // getName
        assertNotNull(var.getName());

        // getMap
        assertNotNull(var.getMap());

        // getList
        assertNotNull(var.getList());

    }

    @Test
    public void subClasses(){
        QQueryExtensionsTest_SubPoint var = new QQueryExtensionsTest_SubPoint(PathMetadataFactory.forVariable("var"));
        var.getName();
        var.getMap();
        var.getList();
    }
    
    @Test
    public void cachedMethods(){
        QQueryExtensionsTest_Point var = new QQueryExtensionsTest_Point(PathMetadataFactory.forVariable("var"));        
        assertTrue(var.getName() == var.getName());
    }
}
