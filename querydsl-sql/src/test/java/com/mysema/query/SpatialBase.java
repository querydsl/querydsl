package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.mysema.query.spatial.PointExpression;
import com.mysema.query.spatial.path.PointPath;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.spatial.QShapes;
import com.mysema.query.sql.spatial.Shapes;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;

public class SpatialBase extends AbstractBaseTest {

    // TEMPORARY
    @BeforeClass
    public static void setUp() throws Exception {
//        Connections.initTeradata();
//        Connections.setTemplates(TeradataTemplates.builder().newLineToSingleSpace().build());
        Connections.initPostgres();
        Connections.setTemplates(PostgresTemplates.builder().quote().newLineToSingleSpace().build());
    }

    // TEMPORARY
    @AfterClass
    public static void tearDown() throws SQLException {
        Connections.close();
    }

    @Test
    public void Point_Instances() {
        QShapes shapes = QShapes.shapes;
        List<Shapes> results = query().from(shapes).list(shapes);
        assertEquals(5, results.size());
        for (Shapes row : results) {
            assertNotNull(row.getId());
            assertNotNull(row.getGeometry());
            assertTrue(row.getGeometry() instanceof Point);
        }
    }

    @Test
    public void Point_Methods() {
        QShapes shapes = QShapes.shapes;
        PointPath<Point> point = new PointPath<Point>(Point.class, shapes, "geometry");

        List<Expression<?>> expressions = Lists.newArrayList();
        expressions.add(point.asBinary());
        expressions.add(point.asText());
        expressions.add(point.boundary());
        expressions.add(point.convexHull());
        expressions.add(point.dimension());
        expressions.add(point.envelope());
        expressions.add(point.geometryType());
        expressions.add(point.isEmpty());
        expressions.add(point.isSimple());
//        expressions.add(point.m());
        expressions.add(point.srid());
        expressions.add(point.x());
        expressions.add(point.y());
//        expressions.add(point.z());

        for (Expression<?> expr : expressions) {
            for (Object row : query().from(shapes).list(expr)) {
                assertNotNull(expr.toString(), row);
            }
        }
    }

    private List<Expression<?>> createExpressions(PointExpression<Point> point1, Expression<Point> point2) {
        List<Expression<?>> expressions = Lists.newArrayList();
        expressions.add(point1.contains(point2));
//        expressions.add(point1.crosses(point2));
        expressions.add(point1.difference(point2));
        expressions.add(point1.disjoint(point2));
        expressions.add(point1.distance(point2));
        expressions.add(point1.eq(point2));
        expressions.add(point1.intersection(point2));
        expressions.add(point1.intersects(point2));
        expressions.add(point1.overlaps(point2));
        expressions.add(point1.symDifference(point2));
//        expressions.add(point1.touches(point2));
        expressions.add(point1.union(point2));
        expressions.add(point1.within(point2));
        return expressions;
    }

    @Test
    public void Point_Methods2() {
        QShapes shapes1 = QShapes.shapes;
        PointPath<Point> point1 = new PointPath<Point>(Point.class, shapes1, "geometry");

        QShapes shapes2 = new QShapes("shapes2");
        PointPath<Point> point2 = new PointPath<Point>(Point.class, shapes2, "geometry");

        List<Expression<?>> expressions = Lists.newArrayList();
        expressions.addAll(createExpressions(point1, point2));
        expressions.addAll(createExpressions(point1, ConstantImpl.create((Point)Wkt.fromWkt("Point(2 2)"))));

        for (Expression<?> expr : expressions) {
            for (Object row : query().from(shapes1, shapes2).list(expr)) {
                assertNotNull(expr.toString(), row);
            }
        }
    }

}
