package com.querydsl.sql.spatial;

import java.util.List;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.PointSequence;
import org.geolatte.geom.PointSequenceBuilder;
import org.geolatte.geom.PointSequenceBuilders;
import org.geolatte.geom.Points;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CrsId;

import com.google.common.collect.Lists;
import com.querydsl.sql.Connections;

public abstract class AbstractConverterTest {

    protected PointSequence createSequence(CrsId crs, Point... points) {
        PointSequenceBuilder builder = PointSequenceBuilders.fixedSized(points.length, points[0].getDimensionalFlag(), crs);
        for (Point point : points) {
            builder.add(point);
        }
        return builder.toPointSequence();
    }

    protected List<Geometry> getGeometries() {
        CrsId crs = CrsId.valueOf(1);
        List<Geometry> data = Lists.newArrayList();
        // points
//        data.add(Points.createEmpty());
        data.add(Points.create2D(1, 2));
        data.add(Points.create2D(1, 2, crs));
        data.add(Points.create3D(1, 2, 3));
        data.add(Points.create3D(1, 2, 3, crs));
        data.add(Points.create2DM(1, 2, 3));
        data.add(Points.create2DM(1, 2, 3, crs));

        // linestring
        data.add(LineString.createEmpty());
        for (int i = 0; i < 6; i++) {
            data.add(new LineString(createSequence(crs, (Point)data.get(i), (Point)data.get(i))));
        }

        // polgyon
        // TODO

        // multipoint
        data.add(MultiPoint.createEmpty());
        for (int i = 0; i < 6; i++) {
            data.add(new MultiPoint(new Point[]{(Point)data.get(i)}));
        }

        // multilinestring
        int size = data.size();
        data.add(MultiLineString.createEmpty());
        for (int i = 0; i < size; i++) {
            if (data.get(i) instanceof LineString) {
                data.add(new MultiLineString(new LineString[]{(LineString)data.get(i)}));
            }
        }

        // multipolygon
        data.add(MultiPolygon.createEmpty());
        for (int i = 0; i < size; i++) {
            if (data.get(i) instanceof Polygon) {
                data.add(new MultiPolygon(new Polygon[]{(Polygon)data.get(i)}));
            }
        }

        // collection
        size = data.size();
        for (int i = 0; i < size; i++) {
            data.add(new GeometryCollection(new Geometry[]{data.get(i)}));
        }

        for (String wkt : Connections.getSpatialData().values()) {
            data.add(Wkt.fromWkt(wkt));
        }

        return data;
    }

}
