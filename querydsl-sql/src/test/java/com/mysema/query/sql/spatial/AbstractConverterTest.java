package com.mysema.query.sql.spatial;

import java.util.List;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.Point;
import org.geolatte.geom.PointSequence;
import org.geolatte.geom.PointSequenceBuilder;
import org.geolatte.geom.PointSequenceBuilders;
import org.geolatte.geom.Points;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CrsId;

import com.google.common.collect.Lists;
import com.mysema.query.Connections;

public abstract class AbstractConverterTest {

    protected PointSequence createSequence(Point... points) {
        PointSequenceBuilder builder = PointSequenceBuilders.fixedSized(points.length, points[0].getDimensionalFlag());
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
        data.add(Points.create(1, 2));
        data.add(Points.create(1, 2, crs));
        data.add(Points.create3D(1, 2, 3));
        data.add(Points.create3D(1, 2, 3, crs));
        data.add(Points.createMeasured(1, 2, 3));
        data.add(Points.createMeasured(1, 2, 3, crs));
        // linestring
        data.add(LineString.createEmpty());
        for (int i = 0; i < 6; i++) {
            data.add(new LineString(createSequence((Point)data.get(i)), crs));
        }
        // polgyon
        // TODO
        // multipoint
        data.add(MultiPoint.createEmpty());
        for (int i = 0; i < 6; i++) {
            data.add(new MultiPoint(new Point[]{(Point)data.get(i)}));
        }
        // multilinestring
        // TODO
        // multipolygon
        // TODO

        for (String wkt : Connections.getSpatialData().values()) {
            data.add(Wkt.fromWkt(wkt));
        }

        return data;
    }

}
