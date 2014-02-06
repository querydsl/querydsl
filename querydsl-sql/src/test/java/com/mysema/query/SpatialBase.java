package com.mysema.query;

import static com.mysema.query.Target.H2;
import static com.mysema.query.Target.MYSQL;
import static com.mysema.query.Target.TERADATA;
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
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.spatial.QShapes;
import com.mysema.query.sql.spatial.Shapes;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;

public class SpatialBase extends AbstractBaseTest {

    // H2 - OK
    // MySQL - OK
    // Oracle Spatial - TODO
    // Postgres - OK
    // SQL Server - TODO
    // Teradata - OK

    // TEMPORARY
    @BeforeClass
    public static void setUp() throws Exception {
//        Connections.initTeradata();
//        Connections.setTemplates(TeradataTemplates.builder().newLineToSingleSpace().build());

//        Connections.initPostgres();
//        Connections.setTemplates(PostGISTemplates.builder().quote().newLineToSingleSpace().build());

//        Connections.initSQLServer();
//        Connections.setTemplates(SQLServer2008Templates.builder().newLineToSingleSpace().build());

//        Connections.initMySQL();
//        Connections.setTemplates(MySQLTemplates.builder().newLineToSingleSpace().build());

        Connections.initH2();
        Connections.setTemplates(H2Templates.builder().newLineToSingleSpace().build());
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
        add(expressions, point.x(), H2);
        add(expressions, point.y(), H2);
        add(expressions, point.z(), MYSQL, TERADATA, H2);

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : query().from(shapes).list(expr)) {
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
        PointPath<Point> point1 = new PointPath<Point>(Point.class, shapes1, "geometry");

        QShapes shapes2 = new QShapes("shapes2");
        PointPath<Point> point2 = new PointPath<Point>(Point.class, shapes2, "geometry");

        List<Expression<?>> expressions = Lists.newArrayList();
        expressions.addAll(createExpressions(point1, point2));
        expressions.addAll(createExpressions(point1, ConstantImpl.create((Point)Wkt.fromWkt("Point(2 2)"))));

        for (Expression<?> expr : expressions) {
            boolean logged = false;
            for (Object row : query().from(shapes1, shapes2).list(expr)) {
                if (row == null && !logged) {
                    System.err.println(expr.toString());
                    logged = true;
                }
            }
        }
    }

}
