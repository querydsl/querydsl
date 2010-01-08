package com.mysema.query.domain;

import static org.junit.Assert.*;

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
        
        
    }
        
    @Test
    public void test(){
        QQueryExtensionsTest_Point var = new QQueryExtensionsTest_Point(PathMetadataFactory.forVariable("var"));
        QQueryExtensionsTest_Point var2 = new QQueryExtensionsTest_Point(PathMetadataFactory.forVariable("var2"));
        assertEquals("geo_distance(var, var2)", var.geoDistance(var2).toString());
    }
}
