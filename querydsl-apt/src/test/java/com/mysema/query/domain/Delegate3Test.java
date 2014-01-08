package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.template.BooleanTemplate;

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
