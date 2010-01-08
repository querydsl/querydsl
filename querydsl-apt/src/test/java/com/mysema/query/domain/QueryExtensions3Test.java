package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.path.PathMetadataFactory;

public class QueryExtensions3Test {

    public static class Point{

        @QueryMethod("geo_distance({0}, {1})")
        int geoDistance(Point otherPoint){
            return 0;
        }
        
    }
    
    @Test
    public void test(){
        QQueryExtensions3Test_Point var = new QQueryExtensions3Test_Point(PathMetadataFactory.forVariable("var"));
        QQueryExtensions3Test_Point var2 = new QQueryExtensions3Test_Point(PathMetadataFactory.forVariable("var2"));
        
        // geoDistance
        assertEquals("geo_distance(var, var2)", var.geoDistance(var2).toString());
    }
}
