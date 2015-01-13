package com.querydsl.sql.spatial;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import org.geolatte.geom.Geometry;

import com.querydsl.spatial.path.GeometryPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;

/**
 * QShapes is a Querydsl querydsl type for QShapes
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QShapes extends RelationalPathSpatial<Shapes> {

    private static final long serialVersionUID = 563213127;

    public static final QShapes shapes = new QShapes("SHAPES");

    public final GeometryPath<Geometry> geometry = createGeometry("geometry", Geometry.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.querydsl.sql.PrimaryKey<Shapes> shapesPkey = createPrimaryKey(id);

    public QShapes(String variable) {
        super(Shapes.class, forVariable(variable), "PUBLIC", "SHAPES");
        addMetadata();
    }

    public QShapes(String variable, String schema, String table) {
        super(Shapes.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QShapes(Path<? extends Shapes> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "SHAPES");
        addMetadata();
    }

    public QShapes(PathMetadata<?> metadata) {
        super(Shapes.class, metadata, "PUBLIC", "SHAPES");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(geometry, ColumnMetadata.named("GEOMETRY").ofType(1111).withSize(2147483647));
        addMetadata(id, ColumnMetadata.named("ID").ofType(4).withSize(10).notNull());
    }

}

