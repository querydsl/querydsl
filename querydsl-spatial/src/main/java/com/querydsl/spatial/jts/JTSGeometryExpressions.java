package com.querydsl.spatial.jts;

import com.querydsl.spatial.SpatialOps;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.BooleanOperation;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.NumberOperation;
import com.querydsl.core.types.expr.StringExpression;
import com.querydsl.core.types.expr.StringOperation;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * GeometryExpressions contains static functions for GEO operations
 */
public final class JTSGeometryExpressions {

    /**
     * Return a specified ST_Geometry value from Extended Well-Known Text representation (EWKT).
     *
     * @param expr
     * @return
     */
    public static StringExpression asEWKT(JTSGeometryExpression<?> expr) {
        return StringOperation.create(SpatialOps.AS_EWKT, expr);
    }

    /**
     * Return a specified ST_Geometry value from Well-Known Text representation (WKT).
     *
     * @param text
     * @return
     */
    public static JTSGeometryExpression<?> fromText(String text) {
        return JTSGeometryOperation.create(Geometry.class, SpatialOps.GEOM_FROM_TEXT, ConstantImpl.create(text));
    }

    /**
     * Return a specified ST_Geometry value from Well-Known Text representation (WKT).
     *
     * @param text
     * @return
     */
    public static JTSGeometryExpression<?> fromText(Expression<String> text) {
        return JTSGeometryOperation.create(Geometry.class, SpatialOps.GEOM_FROM_TEXT, text);
    }

    /**
     * Sets the SRID on a geometry to a particular integer value.
     *
     * @param expr
     * @param srid
     * @param <T>
     * @return
     */
    public static <T extends Geometry> JTSGeometryExpression<T> setSRID(Expression<T> expr, int srid) {
        return (JTSGeometryExpression)JTSGeometryOperation.create(expr.getType(), SpatialOps.SET_SRID,
                expr, ConstantImpl.create(srid));
    }

    /**
     * Returns X minima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> xmin(JTSGeometryExpression<?> expr) {
        return NumberOperation.create(Double.class, SpatialOps.XMIN, expr);
    }

    /**
     * Returns X maxima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> xmax(JTSGeometryExpression<?> expr) {
        return NumberOperation.create(Double.class, SpatialOps.XMAX, expr);
    }

    /**
     * Returns Y minima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> ymin(JTSGeometryExpression<?> expr) {
        return NumberOperation.create(Double.class, SpatialOps.YMIN, expr);
    }

    /**
     * Returns Y maxima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> ymax(JTSGeometryExpression<?> expr) {
        return NumberOperation.create(Double.class, SpatialOps.YMAX, expr);
    }

    /**
     * Returns true if the geometries are within the specified distance of one another.
     * For geometry units are in those of spatial reference and For geography units are in meters.
     *
     * @param expr1
     * @param expr2
     * @param distance
     * @return
     */
    public static BooleanExpression dwithin(Expression<? extends Geometry> expr1,
                                            Expression<? extends Geometry> expr2, Expression<Double> distance) {
        return BooleanOperation.create(SpatialOps.DWITHIN, expr1, expr2, distance);
    }

    /**
     * Returns true if the geometries are within the specified distance of one another.
     * For geometry units are in those of spatial reference and For geography units are in meters.
     *
     * @param expr1
     * @param expr2
     * @param distance
     * @return
     */
    public static BooleanExpression dwithin(Expression<? extends Geometry> expr1,
                                            Expression<? extends Geometry> expr2, double distance) {
        return BooleanOperation.create(SpatialOps.DWITHIN, expr1, expr2, ConstantImpl.create(distance));
    }

    /**
     *  Returns the bounding box that bounds rows of geometries.
     *
     * @param collection
     * @return
     */
    public static JTSGeometryExpression<?> extent(Expression<? extends GeometryCollection> collection) {
        return JTSGeometryOperation.create(Geometry.class, SpatialOps.EXTENT, collection);
    }

    /**
     * Return a specified ST_Geometry value from a collection of other geometries.
     *
     * @param collection
     * @return
     */
    public static JTSGeometryExpression<?> collect(Expression<? extends GeometryCollection> collection) {
        return JTSGeometryOperation.create(Geometry.class, SpatialOps.COLLECT, collection);
    }

    /**
     * Return a specified ST_Geometry value from a collection of other geometries.
     *
     * @param expr1
     * @param expr2
     * @return
     */
    public static JTSGeometryExpression<?> collect(Expression<? extends Geometry> expr1, Expression<? extends Geometry> expr2) {
        return JTSGeometryOperation.create(Geometry.class, SpatialOps.COLLECT2, expr1, expr2);
    }

    /**
     * Translates the geometry to a new location using the numeric parameters as offsets.
     *
     * @param expr
     * @param deltax
     * @param deltay
     * @param <T>
     * @return
     */
    public static <T extends Geometry> JTSGeometryExpression<T> translate(Expression<T> expr, float deltax, float deltay) {
        return (JTSGeometryExpression)JTSGeometryOperation.create(expr.getType(), SpatialOps.TRANSLATE,
                expr, ConstantImpl.create(deltax), ConstantImpl.create(deltay));
    }

    /**
     * Translates the geometry to a new location using the numeric parameters as offsets.
     *
     * @param expr
     * @param deltax
     * @param deltay
     * @param deltaz
     * @param <T>
     * @return
     */
    public static <T extends Geometry> JTSGeometryExpression<T> translate(Expression<T> expr, float deltax, float deltay, float deltaz) {
        return (JTSGeometryExpression)JTSGeometryOperation.create(expr.getType(), SpatialOps.TRANSLATE2,
                expr, ConstantImpl.create(deltax), ConstantImpl.create(deltay), ConstantImpl.create(deltaz));
    }

    private JTSGeometryExpressions() {}
}
