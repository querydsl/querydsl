/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.extensions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.ComparablePath;

public class QueryExtensions3Test {

    public static class Point implements Comparable<Point>{

        @QueryMethod("geo_distance({0}, {1})")
        int geoDistance(Point otherPoint){
            return 0;
        }

        @Override
        public int compareTo(Point o) {
            return 0;
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o){
            return o instanceof Point;
        }

    }

    @QueryEntity
    public static class Entity{

        Point point;
    }

    @Test
    public void test(){
        QQueryExtensions3Test_Point var = new QQueryExtensions3Test_Point(PathMetadataFactory.forVariable("var"));
        QQueryExtensions3Test_Point var2 = new QQueryExtensions3Test_Point(PathMetadataFactory.forVariable("var2"));

        // geoDistance
        assertEquals("geo_distance(var, var2)", var.geoDistance(var2).toString());
    }

    @Test
    public void test_Point_has_right_type(){
        assertEquals(ComparablePath.class, QQueryExtensions3Test_Point.class.getSuperclass());
        assertEquals(ComparablePath.class, QQueryExtensions3Test_Entity.entity.point.getClass().getSuperclass());
    }
}
