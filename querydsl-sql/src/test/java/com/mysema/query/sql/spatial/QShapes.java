package com.mysema.query.sql.spatial;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import org.geolatte.geom.Geometry;

import com.mysema.query.spatial.path.GeometryPath;
import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.NumberPath;

/**
 * QShapes is a Querydsl query type for QShapes
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QShapes extends com.mysema.query.sql.RelationalPathBase<Shapes> {

    private static final long serialVersionUID = 563213127;

    public static final QShapes shapes = new QShapes("SHAPES");

    public final GeometryPath<Geometry> geometry = add(new GeometryPath<Geometry>(Geometry.class,
            PathMetadataFactory.forProperty(this, "geometry")));

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<Shapes> shapesPkey = createPrimaryKey(id);

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

