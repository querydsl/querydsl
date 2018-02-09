/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.spatial;

import org.geolatte.geom.*;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;

/**
 * GeometryExpressions contains static functions for GEO operations
 */
public final class GeometryExpressions {

    private GeometryExpressions() { }

    /**
     * Return a specified ST_Geometry value from Extended Well-Known Text representation (EWKT).
     *
     * @param expr geometry
     * @return serialized form
     */
    public static StringExpression asEWKT(GeometryExpression<?> expr) {
        return Expressions.stringOperation(SpatialOps.AS_EWKT, expr);
    }

    /**
     * Return a specified ST_Geometry value from Well-Known Text representation (WKT).
     *
     * @param text WKT form
     * @return geometry
     */
    public static GeometryExpression<?> fromText(String text) {
        return geometryOperation(SpatialOps.GEOM_FROM_TEXT, ConstantImpl.create(text));
    }

    /**
     * Return a specified ST_Geometry value from Well-Known Text representation (WKT).
     *
     * @param text WKT form
     * @return geometry
     */
    public static GeometryExpression<?> fromText(Expression<String> text) {
        return geometryOperation(SpatialOps.GEOM_FROM_TEXT, text);
    }

    /**
     * Sets the SRID on a geometry to a particular integer value.
     *
     * @param expr geometry
     * @param srid SRID
     * @param <T>
     * @return converted geometry
     */
    public static <T extends Geometry> GeometryExpression<T> setSRID(Expression<T> expr, int srid) {
        return geometryOperation(expr.getType(), SpatialOps.SET_SRID, expr, ConstantImpl.create(srid));
    }

    /**
     * Returns X minima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr geometry
     * @return x minima
     */
    public static NumberExpression<Double> xmin(GeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.XMIN, expr);
    }

    /**
     * Returns X maxima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr geometry
     * @return x maxima
     */
    public static NumberExpression<Double> xmax(GeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.XMAX, expr);
    }

    /**
     * Returns Y minima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr geometry
     * @return y minima
     */
    public static NumberExpression<Double> ymin(GeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.YMIN, expr);
    }

    /**
     * Returns Y maxima of a bounding box 2d or 3d or a geometry.
     *
     * @param expr geometry
     * @return y maxima
     */
    public static NumberExpression<Double> ymax(GeometryExpression<?> expr) {
        return Expressions.numberOperation(Double.class, SpatialOps.YMAX, expr);
    }

    /**
     * Returns true if the geometries are within the specified distance of one another.
     * For geometry units are in those of spatial reference and For geography units are in meters.
     *
     * @param expr1 geometry
     * @param expr2 other geometry
     * @param distance distance
     * @return true, if within distance of each other
     */
    public static BooleanExpression dwithin(
            Expression<? extends Geometry> expr1, Expression<? extends Geometry> expr2, Expression<Double> distance) {

        return Expressions.booleanOperation(SpatialOps.DWITHIN, expr1, expr2, distance);
    }

    /**
     * Returns true if the geometries are within the specified distance of one another.
     * For geometry units are in those of spatial reference and For geography units are in meters.
     *
     * @param expr1 geometry
     * @param expr2 other geometry
     * @param distance distance
     * @return true, if within distance of each other
     */
    public static BooleanExpression dwithin(
            Expression<? extends Geometry> expr1, Expression<? extends Geometry> expr2, double distance) {

        return Expressions.booleanOperation(SpatialOps.DWITHIN, expr1, expr2, ConstantImpl.create(distance));
    }

    /**
     *  Returns the bounding box that bounds rows of geometries.
     *
     * @param collection geometry collection
     * @return bounding box
     */
    public static GeometryExpression<?> extent(Expression<? extends GeometryCollection> collection) {
        return geometryOperation(SpatialOps.EXTENT, collection);
    }

    /**
     * Return a specified ST_Geometry value from a collection of other geometries.
     *
     * @param collection geometry collection
     * @return geometry collection
     */
    public static GeometryExpression<?> collect(Expression<? extends GeometryCollection> collection) {
        return geometryOperation(SpatialOps.COLLECT, collection);
    }

    /**
     * Return a specified ST_Geometry value from a collection of other geometries.
     *
     * @param expr1 geometry
     * @param expr2 other geometry
     * @return  geometry collection
     */
    public static GeometryExpression<?> collect(Expression<? extends Geometry> expr1, Expression<? extends Geometry> expr2) {
        return geometryOperation(SpatialOps.COLLECT2, expr1, expr2);
    }

    /**
     * Translates the geometry to a new location using the numeric parameters as offsets.
     *
     * @param expr geometry
     * @param deltax x offset
     * @param deltay y offset
     * @param <T>
     * @return geometry
     */
    public static <T extends Geometry> GeometryExpression<T> translate(Expression<T> expr, float deltax, float deltay) {
        return geometryOperation(expr.getType(), SpatialOps.TRANSLATE,
                expr, ConstantImpl.create(deltax), ConstantImpl.create(deltay));
    }

    /**
     * Translates the geometry to a new location using the numeric parameters as offsets.
     *
     * @param expr geometry
     * @param deltax x offset
     * @param deltay y offset
     * @param deltaz z offset
     * @param <T>
     * @return geometry
     */
    public static <T extends Geometry> GeometryExpression<T> translate(Expression<T> expr, float deltax, float deltay, float deltaz) {
        return geometryOperation(expr.getType(), SpatialOps.TRANSLATE2,
                expr, ConstantImpl.create(deltax), ConstantImpl.create(deltay), ConstantImpl.create(deltaz));
    }

    /**
     * Create a new Geometry operation expression
     *
     * @param op operator
     * @param args arguments
     * @return operation expression
     */
    public static GeometryExpression<Geometry> geometryOperation(Operator op, Expression<?>... args) {
        return new GeometryOperation<Geometry>(Geometry.class, op, args);
    }

    /**
     * Create a new Geometry operation expression
     *
     * @param op operator
     * @param args arguments
     * @return operation expression
     */
    public static <T extends Geometry> GeometryExpression<T> geometryOperation(
            Class<? extends T> type, Operator op, Expression<?>... args) {

        return new GeometryOperation<T>(type, op, args);
    }

    /**
     * Create a new LineString operation expression
     *
     * @param op operator
     * @param args arguments
     * @return operation expression
     */
    public static LineStringExpression<LineString> lineStringOperation(Operator op, Expression<?>... args) {
        return new LineStringOperation<LineString>(LineString.class, op, args);
    }

    /**
     * Create a new Point operation expression
     *
     * @param op operator
     * @param args arguments
     * @return operation expression
     */
    public static PointExpression<Point> pointOperation(Operator op, Expression<?>... args) {
        return new PointOperation<Point>(Point.class, op, args);
    }

    /**
     * Create a new Polygon operation expression
     *
     * @param op operator
     * @param args arguments
     * @return operation expression
     */
    public static PolygonExpression<Polygon> polygonOperation(Operator op, Expression<?>... args) {
        return new PolygonOperation<Polygon>(Polygon.class, op, args);
    }

    /**
     * Create a new GeometryExpression
     *
     * @param expr Expression of type Geometry
     * @return new GeometryExpression
     */
    public static <T extends Geometry> GeometryExpression<T> asGeometry(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new GeometryPath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new GeometryOperation<T>((OperationImpl<T>) underlyingMixin);
        } else {
            return new GeometryExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = -6714044005570420009L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

            };
        }
    }

    /**
     * Create a new GeometryExpression
     *
     * @param value Geometry
     * @return new GeometryExpression
     */
    public static <T extends Geometry> GeometryExpression<T> asGeometry(T value) {
        return asGeometry(Expressions.constant(value));
    }

}
