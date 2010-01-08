package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;

public class QueryExtensions2Test {
    
    @QueryEmbeddable
    public static class Point{
        
    }
    
    @QueryExtensions(Point.class)
    public interface PointOperations {

       @QueryMethod("geo_distance({0},{1})")
       int geoDistance(Point otherPoint);

    }
    
    @Test
    public void test(){
        
    }

}
