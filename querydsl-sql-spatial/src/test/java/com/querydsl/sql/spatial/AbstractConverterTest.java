package com.querydsl.sql.spatial;

import com.google.common.collect.Lists;
import com.querydsl.sql.Connections;
import org.geolatte.geom.*;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import java.util.List;

public abstract class AbstractConverterTest {

    protected List<Geometry> getGeometries() {
        List<Geometry> data = Lists.newArrayList();
        // points
        data.add(DSL.point(CoordinateReferenceSystems.PROJECTED_2D_METER, DSL.c(1, 2)));
        data.add(DSL.point(CoordinateReferenceSystems.PROJECTED_2DM_METER, DSL.cM(1, 2, 3)));
        data.add(DSL.point(CoordinateReferenceSystems.PROJECTED_3D_METER, DSL.c(1, 2, 3)));
        data.add(DSL.point(CoordinateReferenceSystems.PROJECTED_3DM_METER, DSL.c(1, 2, 3, 4)));
        data.add(DSL.point(CoordinateReferenceSystems.WGS84, DSL.g(1, 2)));
        data.add(DSL.point(CoordinateReferenceSystems.WEB_MERCATOR, DSL.c(1, 2)));

        // linestring
        data.add(Geometries.mkEmptyLineString(CoordinateReferenceSystems.PROJECTED_2D_METER));

        for (int i = 0; i < 6; i++) {
            final Point point = (Point) data.get(i);
            data.add(DSL.linestring(point.getCoordinateReferenceSystem(), point.getPosition(), point.getPosition()));
        }

        // polygon
        data.add(Geometries.mkPolygon(Geometries.mkLinearRing(PositionSequenceBuilders.fixedSized(5, C2D.class)
                .add(1, 1).add(2, 1).add(2, 2).add(2, 1).add(1, 1)
                .toPositionSequence(), CoordinateReferenceSystems.PROJECTED_2D_METER)));
        data.add(Geometries.mkPolygon(Geometries.mkLinearRing(PositionSequenceBuilders.fixedSized(5, C3D.class)
                .add(1, 1, 1).add(2, 1, 1).add(2, 2, 1).add(2, 1, 1).add(1, 1, 1)
                .toPositionSequence(), CoordinateReferenceSystems.PROJECTED_3D_METER)));
        data.add(Geometries.mkPolygon(Geometries.mkLinearRing(PositionSequenceBuilders.fixedSized(5, G2D.class)
                .add(1, 1).add(2, 1).add(2, 2).add(2, 1).add(1, 1)
                .toPositionSequence(), CoordinateReferenceSystems.WGS84)));

        // multipoint
        data.add(Geometries.mkEmptyMultiPoint(CoordinateReferenceSystems.PROJECTED_2D_METER));
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (data.get(i) instanceof Point) {
                data.add(Geometries.mkMultiPoint((Point) data.get(i)));
            }
        }

        // multilinestring
        data.add(Geometries.mkEmptyMultiLineString(CoordinateReferenceSystems.PROJECTED_2D_METER));
        size = data.size();
        for (int i = 0; i < size; i++) {
            if (data.get(i) instanceof LineString && !data.get(i).isEmpty()) {
                data.add(Geometries.mkMultiLineString((LineString) data.get(i)));
            }
        }

        // multipolygon
        data.add(Geometries.mkEmptyMultiPolygon(CoordinateReferenceSystems.PROJECTED_2D_METER));
        size = data.size();
        for (int i = 0; i < size; i++) {
            if (data.get(i) instanceof Polygon) {
                data.add(Geometries.mkMultiPolygon((Polygon) data.get(i)));
            }
        }

        // collection
        data.add(Geometries.mkEmptyGeometryCollection(CoordinateReferenceSystems.PROJECTED_2D_METER));
        size = data.size();
        for (int i = 0; i < size; i++) {
            if (!data.get(i).isEmpty()) {
                data.add(Geometries.mkGeometryCollection(data.get(i)));
            }
        }

        for (String wkt : Connections.getSpatialData().values()) {
            data.add(Wkt.fromWkt(wkt));
        }

        return data;
    }

}
