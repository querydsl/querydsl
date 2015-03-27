package com.querydsl.spatial.jts;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.spatial.SpatialOps;
import com.vividsolutions.jts.geom.*;

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
        return Expressions.stringOperation(SpatialOps.AS_EWKT, expr);
    }

    /**
     * Return a specified ST_Geometry value from Well-Known Text representation (WKT).
     *
     * @param text
     * @return
     */
    public static JTSGeometryExpression<?> fromText(String text) {
        return geometryOperation(SpatialOps.GEOM_FROM_TEXT, ConstantImpl.create(text));
    }

    /**
     * Return a specified ST_Geometry value from Well-Known Text representation (WKT).
     *
     * @param text
     * @return
     */
    public static JTSGeometryExpression<?> fromText(Expression<String> text) {
        return geometryOperation(SpatialOps.GEOM_FROM_TEXT, text);
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
        return geometryOperation(expr.getType(), SpatialOps.SET_SRID,
                expr, ConstantImpl.create(srid));
    }

    /**
     * Returns X minima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> xmin(JTSGeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.XMIN, expr);
    }

    /**
     * Returns X maxima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> xmax(JTSGeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.XMAX, expr);
    }

    /**
     * Returns Y minima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> ymin(JTSGeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.YMIN, expr);
    }

    /**
     * Returns Y maxima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr
     * @return
     */
    public static NumberExpression<Double> ymax(JTSGeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.YMAX, expr);
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
        return Expressions.booleanOperation(SpatialOps.DWITHIN, expr1, expr2, distance);
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
        return Expressions.booleanOperation(SpatialOps.DWITHIN, expr1, expr2, ConstantImpl.create(distance));
    }

    /**
     *  Returns the bounding box that bounds rows of geometries.
     *
     * @param collection
     * @return
     */
    public static JTSGeometryExpression<?> extent(Expression<? extends GeometryCollection> collection) {
        return geometryOperation(SpatialOps.EXTENT, collection);
    }

    /**
     * Return a specified ST_Geometry value from a collection of other geometries.
     *
     * @param collection
     * @return
     */
    public static JTSGeometryExpression<?> collect(Expression<? extends GeometryCollection> collection) {
        return geometryOperation(SpatialOps.COLLECT, collection);
    }

    /**
     * Return a specified ST_Geometry value from a collection of other geometries.
     *
     * @param expr1
     * @param expr2
     * @return
     */
    public static JTSGeometryExpression<?> collect(Expression<? extends Geometry> expr1, Expression<? extends Geometry> expr2) {
        return geometryOperation(SpatialOps.COLLECT2, expr1, expr2);
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
        return geometryOperation(expr.getType(), SpatialOps.TRANSLATE,
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
        return geometryOperation(expr.getType(), SpatialOps.TRANSLATE2,
                expr, ConstantImpl.create(deltax), ConstantImpl.create(deltay), ConstantImpl.create(deltaz));
    }

    /**
     * Create a new Geometry operation expression
     *
     * @param op
     * @param args
     * @return
     */
    public static JTSGeometryExpression<Geometry> geometryOperation(Operator op, Expression<?>... args) {
        return new JTSGeometryOperation<Geometry>(Geometry.class, op, args);
    }

    /**
     * Create a new Geometry operation expression
     *
     * @param op
     * @param args
     * @return
     */
    public static <T extends Geometry> JTSGeometryExpression<T> geometryOperation(Class<? extends T> type,
                                                                                  Operator op, Expression<?>... args) {
        return new JTSGeometryOperation<T>(type, op, args);
    }

    /**
     * Create a new LineString operation expression
     *
     * @param op
     * @param args
     * @return
     */
    public static JTSLineStringExpression<LineString> lineStringOperation(Operator op, Expression<?>... args) {
        return new JTSLineStringOperation<LineString>(LineString.class, op, args);
    }

    /**
     * Create a new Point operation expression
     *
     * @param op
     * @param args
     * @return
     */
    public static JTSPointExpression<Point> pointOperation(Operator op, Expression<?>... args) {
        return new JTSPointOperation<Point>(Point.class, op, args);
    }

    /**
     * Create a new Polygon operation expression
     *
     * @param op
     * @param args
     * @return
     */
    public static JTSPolygonExpression<Polygon> polygonOperation(Operator op, Expression<?>... args) {
        return new JTSPolygonOperation<Polygon>(Polygon.class, op, args);
    }

    private JTSGeometryExpressions() {}
}
