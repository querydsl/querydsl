package com.querydsl.sql;

import java.util.List;

import com.google.common.collect.Lists;
import com.querydsl.core.Target;
import com.querydsl.core.Tuple;
import com.querydsl.spatial.GeometryExpression;
import com.querydsl.spatial.GeometryExpressions;
import com.querydsl.spatial.PointExpression;
import com.querydsl.spatial.path.*;
import com.querydsl.sql.spatial.QShapes;
import com.querydsl.sql.spatial.QSpatialRefSys;
import com.querydsl.sql.spatial.Shapes;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import org.geolatte.geom.*;
import org.geolatte.geom.codec.Wkt;
import org.junit.Test;
import static com.querydsl.core.Target.*;
import static org.junit.Assert.*;

public class SpatialBase extends AbstractBaseTest {

    private static final QShapes shapes = QShapes.shapes;

    // point 1-5
    // linestring 6-7
    // polygon 8-9
    // multipoint 10-11
    // multilinestring 12-13
    // multipolygon 14-15

    private TestQuery withPoints() {
        return query().from(shapes).where(shapes.id.between(1, 5));
    }

    private TestQuery withLineStrings() {
        return query().from(shapes).where(shapes.id.between(6, 7));
    }

    private TestQuery withPolygons() {
        return query().from(shapes).where(shapes.id.between(8, 9));
    }

    private TestQuery withMultipoints() {
        return query().from(shapes).where(shapes.id.between(10, 11));
    }

    private TestQuery withMultiLineStrings() {
        return query().from(shapes).where(shapes.id.between(12, 13));
    }

    private TestQuery withMultiPolygons() {
        return query().from(shapes).where(shapes.id.between(14, 15));
    }

    @Test
    @IncludeIn(POSTGRES)
    public void SpatialRefSys() {
        QSpatialRefSys spatialRefSys = QSpatialRefSys.spatialRefSys;
        query().from(spatialRefSys).list(spatialRefSys);
    }

    private String normalize(String s) {
        String normalized = s.replace(" ", "").replace("ST_", "").replace("_", "");
        normalized = normalized.substring(normalized.indexOf(';') + 1);
        return normalized.toUpperCase();
    }

    @Test // FIXME, maybe use enum as the type ?!?
    @ExcludeIn(H2)
    public void GeometryType() {
        List<Tuple> results = query().from(shapes).list(shapes.geometry, shapes.geometry.geometryType());
        assertFalse(results.isEmpty());
        for (Tuple row : results) {
            assertEquals(
                    normalize(row.get(shapes.geometry).getGeometryType().name()),
                    normalize(row.get(shapes.geometry.geometryType())));
        }
    }

    @Test
    public void AsText() {
        List<Tuple> results = query().from(shapes).list(shapes.geometry, shapes.geometry.asText());
        assertFalse(results.isEmpty());
        for (Tuple row : results) {
            if (!(row.get(shapes.geometry) instanceof MultiPoint)) {
                assertEquals(
                        normalize(row.get(shapes.geometry).asText()),
                        normalize(row.get(shapes.geometry.asText())));
            }
        }
    }

    @Test
    @ExcludeIn(H2)
    public void Point_X_Y() {
        PointPath<Point> point = shapes.geometry.asPoint();
        List<Tuple> results = withPoints().list(point, point.x(), point.y());
        assertFalse(results.isEmpty());
        for (Tuple row : results) {
            assertEquals(Double.valueOf(row.get(point).getX()), row.get(point.x()));
            assertEquals(Double.valueOf(row.get(point).getY()), row.get(point.y()));
        }
    }

    @Test
    @ExcludeIn(MYSQL)
    public void Point_Distance() {
        QShapes shapes1 = QShapes.shapes;
        QShapes shapes2 = new QShapes("shapes2");
        for (Tuple tuple : query().from(shapes1, shapes2)
                    .where(shapes1.id.loe(5), shapes2.id.loe(5))
                    .list(shapes1.geometry.asPoint(),
                          shapes2.geometry.asPoint(),
                          shapes1.geometry.distance(shapes2.geometry))) {
            Point point1 = tuple.get(shapes1.geometry.asPoint());
            Point point2 = tuple.get(shapes2.geometry.asPoint());
            Double distance = tuple.get(shapes1.geometry.distance(shapes2.geometry));
            assertEquals(point1.distance(point2), distance.doubleValue(), 0.0001);
        }
    }

    @Test
    public void Point_Instances() {
        List<Shapes> results = withPoints().list(shapes);
        assertEquals(5, results.size());
        for (Shapes row : results) {
            assertNotNull(row.getId());
            assertNotNull(row.getGeometry());
            assertTrue(row.getGeometry() instanceof Point);
        }
    }

    @Test
    public void LineString_Instances() {
        List<Geometry> results = withLineStrings().list(shapes.geometry);
        assertFalse(results.isEmpty());
        for (Geometry row : results) {
            assertNotNull(row);
            assertTrue(row instanceof LineString);
        }
    }

    @Test
    public void Polygon_Instances() {
        List<Geometry> results = withPolygons().list(shapes.geometry);
        assertFalse(results.isEmpty());
        for (Geometry row : results) {
            assertNotNull(row);
            assertTrue(row instanceof Polygon);
        }
    }

    @Test
    public void MultiPoint_Instances() {
        List<Geometry> results = withMultipoints().list(shapes.geometry);
        assertFalse(results.isEmpty());
        for (Geometry row : results) {
            assertNotNull(row);
            assertTrue(row instanceof MultiPoint);
        }
    }

    @Test
    public void MultiLineString_Instances() {
        List<Geometry> results = withMultiLineStrings().list(shapes.geometry);
        assertFalse(results.isEmpty());
        for (Geometry row : results) {
            assertNotNull(row);
            assertTrue(row instanceof MultiLineString);
        }
    }

    @Test
    public void MultiPolygon_Instances() {
        List<Geometry> results = withMultiPolygons().list(shapes.geometry);
        assertFalse(results.isEmpty());
        for (Geometry row : results) {
            assertNotNull(row);
            assertTrue(row instanceof MultiPolygon);
        }
    }

    @Test
    public void Point_Methods() {
        PointPath<Point> point = shapes.geometry.asPoint();

        List<Expression<?>> expressions = Lists.newArrayList();
        add(expressions, point.asBinary(), H2);
        add(expressions, point.asText());
        add(expressions, point.boundary(), MYSQL);
        add(expressions, point.convexHull(), MYSQL);
        add(expressions, point.dimension());
        add(expressions, point.envelope(), H2);
        add(expressions, point.geometryType(), H2);
        add(expressions, point.isEmpty());
        add(expressions, point.isSimple());
        add(expressions, point.m(), MYSQL, TERADATA, H2);
        add(expressions, point.srid());
        // TODO add emulations
        add(expressions, point.transform(26986), MYSQL, POSTGRES, SQLSERVER, TERADATA, H2);
        // point specific
        add(expressions, point.x(), H2);
        add(expressions, point.y(), H2);
        add(expressions, point.z(), MYSQL, TERADATA, H2);

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : withPoints().list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

    private List<Expression<?>> createExpressions(PointExpression<Point> point1, Expression<Point> point2) {
        List<Expression<?>> expressions = Lists.newArrayList();
        add(expressions, point1.contains(point2));
        add(expressions, point1.crosses(point2));
        add(expressions, point1.difference(point2), MYSQL);
        add(expressions, point1.disjoint(point2));
        add(expressions, point1.distance(point2), MYSQL);
        add(expressions, point1.distanceSphere(point2), H2, MYSQL, SQLSERVER);
        add(expressions, point1.distanceSpheroid(point2), H2, MYSQL, POSTGRES, SQLSERVER);
        add(expressions, point1.eq(point2));
        add(expressions, point1.intersection(point2), MYSQL);
        add(expressions, point1.intersects(point2));
        add(expressions, point1.overlaps(point2));
        add(expressions, point1.symDifference(point2), MYSQL);
        add(expressions, point1.touches(point2));
        add(expressions, point1.union(point2), MYSQL);
        add(expressions, point1.within(point2));
        return expressions;
    }

    @Test
    public void Point_Methods2() {
        QShapes shapes1 = QShapes.shapes;
        QShapes shapes2 = new QShapes("shapes2");

        List<Expression<?>> expressions = Lists.newArrayList();
        expressions.addAll(createExpressions(shapes1.geometry.asPoint(), shapes2.geometry.asPoint()));
        expressions.addAll(createExpressions(shapes1.geometry.asPoint(), ConstantImpl.create((Point)Wkt.fromWkt("Point(2 2)"))));

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : query().from(shapes1, shapes2)
                    .where(shapes1.id.loe(5), shapes2.id.loe(5)).list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

    @Test
    public void LineString_Methods() {
        LineStringPath<LineString> lineString = shapes.geometry.asLineString();

        List<Expression<?>> expressions = Lists.newArrayList();
        add(expressions, lineString.asBinary(), H2);
        add(expressions, lineString.asText());
        add(expressions, lineString.boundary(), MYSQL);
        add(expressions, lineString.convexHull(), MYSQL);
        add(expressions, lineString.dimension());
        add(expressions, lineString.envelope(), H2);
        add(expressions, lineString.geometryType(), H2);
        add(expressions, lineString.isEmpty());
        add(expressions, lineString.isSimple());
        // curve specific
        add(expressions, lineString.length(), H2);
        add(expressions, lineString.startPoint(), H2);
        add(expressions, lineString.endPoint(), H2);
        add(expressions, lineString.isClosed(), H2);
        add(expressions, lineString.isRing(), H2, MYSQL);
        // linestring specific
        add(expressions, lineString.numPoints(), H2);
        add(expressions, lineString.pointN(1), H2);

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : withLineStrings().list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

    @Test
    public void Polygon_Methods() {
        PolygonPath<Polygon> polygon = shapes.geometry.asPolygon();

        List<Expression<?>> expressions = Lists.newArrayList();
        add(expressions, polygon.asBinary(), H2);
        add(expressions, polygon.asText());
        add(expressions, polygon.boundary(), MYSQL);
        add(expressions, polygon.convexHull(), MYSQL);
        add(expressions, polygon.dimension());
        add(expressions, polygon.envelope(), H2);
        add(expressions, polygon.geometryType(), H2);
        add(expressions, polygon.isEmpty());
        add(expressions, polygon.isSimple());
        // surface specific
        add(expressions, polygon.area());
        add(expressions, polygon.centroid());
        add(expressions, polygon.pointOnSurface(), H2, MYSQL);
        // polygon specific
        add(expressions, polygon.exteriorRing(), H2);
        add(expressions, polygon.numInteriorRing(), H2);
        add(expressions, polygon.interiorRingN(1), H2);

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : withPolygons().list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

    @Test
    public void MultiPoint_Methods() {
        MultiPointPath<MultiPoint> multipoint = shapes.geometry.asMultiPoint();

        List<Expression<?>> expressions = Lists.newArrayList();
        add(expressions, multipoint.asBinary(), H2);
        add(expressions, multipoint.asText());
        add(expressions, multipoint.boundary(), MYSQL);
        add(expressions, multipoint.convexHull(), MYSQL);
        add(expressions, multipoint.dimension());
        add(expressions, multipoint.envelope(), H2);
        add(expressions, multipoint.geometryType(), H2);
        add(expressions, multipoint.isEmpty());
        add(expressions, multipoint.isSimple());
        // multipoint specific
        add(expressions, multipoint.numGeometries(), H2);
        add(expressions, multipoint.geometryN(1), H2);

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : withMultipoints().list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

    @Test
    public void MultiLineString_Methods() {
        MultiLineStringPath<MultiLineString> multilinestring = shapes.geometry.asMultiLineString();

        List<Expression<?>> expressions = Lists.newArrayList();
        add(expressions, multilinestring.asBinary(), H2);
        add(expressions, multilinestring.asText());
        add(expressions, multilinestring.boundary(), MYSQL);
        add(expressions, multilinestring.convexHull(), MYSQL);
        add(expressions, multilinestring.dimension());
        add(expressions, multilinestring.envelope(), H2);
        add(expressions, multilinestring.geometryType(), H2);
        add(expressions, multilinestring.isEmpty());
        add(expressions, multilinestring.isSimple());
        // multicurve specific
        add(expressions, multilinestring.isClosed(), H2);
        add(expressions, multilinestring.length(), H2);
        // multilinestring specific
        add(expressions, multilinestring.numGeometries(), H2);
        add(expressions, multilinestring.geometryN(1), H2);

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : withMultiLineStrings().list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

    @Test
    public void MultiPolygon_Methods() {
        MultiPolygonPath<MultiPolygon> multipolygon = shapes.geometry.asMultiPolygon();

        List<Expression<?>> expressions = Lists.newArrayList();
        add(expressions, multipolygon.asBinary(), H2);
        add(expressions, multipolygon.asText());
        add(expressions, multipolygon.boundary(), MYSQL);
        add(expressions, multipolygon.convexHull(), MYSQL);
        add(expressions, multipolygon.dimension());
        add(expressions, multipolygon.envelope(), H2);
        add(expressions, multipolygon.geometryType(), H2);
        add(expressions, multipolygon.isEmpty());
        add(expressions, multipolygon.isSimple());
        // multipolygon specific
        add(expressions, multipolygon.numGeometries(), H2);
        add(expressions, multipolygon.geometryN(1), H2);

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : withMultiPolygons().list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

    @Test
    @IncludeIn(Target.POSTGRES)
    public void Extensions() {
        List<Expression<?>> expressions = Lists.newArrayList();
        GeometryExpression<?> expr1 = shapes.geometry;

        expressions.add(GeometryExpressions.asEWKT(expr1));
        expressions.add(GeometryExpressions.fromText(expr1.asText()));
        expressions.add(GeometryExpressions.setSRID(expr1, 4326));
        expressions.add(GeometryExpressions.xmin(expr1));
        expressions.add(GeometryExpressions.xmax(expr1));
        expressions.add(GeometryExpressions.ymin(expr1));
        expressions.add(GeometryExpressions.ymax(expr1));
        expressions.add(GeometryExpressions.dwithin(expr1, expr1, 1));
        expressions.add(GeometryExpressions.collect(expr1, expr1));
        expressions.add(GeometryExpressions.translate(expr1, 1, 1));

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : withPoints().list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }


}
