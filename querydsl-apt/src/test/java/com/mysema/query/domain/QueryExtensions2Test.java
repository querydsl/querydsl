/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.path.PathMetadataFactory;

public class QueryExtensions2Test {
        
    public static class Point{
        
    }
    
    @QueryExtensions(Point.class)
    public interface PointOperations {

       @QueryMethod("geo_distance({0}, {1})")
       int geoDistance(Point otherPoint);

    }
    
    @QueryEntity
    public static class Entity{
        
        Point point;
    }
    
    @Test
    public void test(){
        QQueryExtensions2Test_Point var = new QQueryExtensions2Test_Point(PathMetadataFactory.forVariable("var"));
        QQueryExtensions2Test_Point var2 = new QQueryExtensions2Test_Point(PathMetadataFactory.forVariable("var2"));
        
        // geoDistance
        assertEquals("geo_distance(var, var2)", var.geoDistance(var2).toString());
    }
    
    @Test
    public void viaEntity(){
        QQueryExtensions2Test_Point point = new QQueryExtensions2Test_Point(PathMetadataFactory.forVariable("point"));
        QQueryExtensions2Test_Entity.entity.point.geoDistance(point);
        
        assertEquals("geo_distance(entity.point, point)", QQueryExtensions2Test_Entity.entity.point.geoDistance(point).toString());
    }

}
