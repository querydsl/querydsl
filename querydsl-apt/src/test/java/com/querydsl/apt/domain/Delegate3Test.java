package com.querydsl.apt.domain;

import org.junit.Test;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.apt.domain.QDelegate3Test_Geometry;
import com.querydsl.apt.domain.QDelegate3Test_Point;
import com.querydsl.apt.domain.QDelegate3Test_Polygon;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.path.ComparablePath;
import com.querydsl.core.types.template.BooleanTemplate;

public class Delegate3Test {

    public static class Geometry implements Comparable<Geometry> {

        @Override
        public int compareTo(Geometry o) {
            return 0;
        }

    }

    public static class Point extends Geometry {

    }

    public static class Polygon extends Geometry {

    }

    @QueryDelegate(Geometry.class)
    public static BooleanExpression isWithin( ComparablePath<? extends Geometry> geo1, ComparablePath<? extends Geometry> geo2){
        return BooleanTemplate.TRUE;
    }

    @Test
    public void test() {
        QDelegate3Test_Geometry.geometry.isWithin(null);
        QDelegate3Test_Point.point.isWithin(null);
        QDelegate3Test_Polygon.polygon.isWithin(null);
    }
}
