package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.template.NumberTemplate;

public class Delegate2Test {

    @QueryEntity
    public static class Entity {
        
        Point point;
    }
    
    public static class Point{

    }

    @QueryDelegate(Point.class)
    public static NumberExpression<Integer> geoDistance(Path<Point> point, Point other){
        return NumberTemplate.create(Integer.class, "geo_distance({0},{1})", point, new ConstantImpl<Point>(other));
    }

    @Test
    public void test(){
        QDelegate2Test_Entity entity = QDelegate2Test_Entity.entity;
        assertNotNull(entity.point.geoDistance(new Point()));
    }

}
