package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;

import java.io.*;

import java.io.File;


/**
 * SShapes is a Querydsl query type for SShapes
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SShapes extends com.mysema.query.sql.RelationalPathBase<SShapes> {

    private static final long serialVersionUID = 844563747;

    public static final SShapes shapes = new SShapes("SHAPES");

    public final SimplePath<byte[]> geometry = createSimple("geometry", byte[].class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SShapes> primary = createPrimaryKey(id);

    public SShapes(String variable) {
        super(SShapes.class, forVariable(variable), "null", "SHAPES");
        addMetadata();
    }

    public SShapes(String variable, String schema, String table) {
        super(SShapes.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SShapes(Path<? extends SShapes> path) {
        super(path.getType(), path.getMetadata(), "null", "SHAPES");
        addMetadata();
    }

    public SShapes(PathMetadata<?> metadata) {
        super(SShapes.class, metadata, "null", "SHAPES");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(geometry, ColumnMetadata.named("GEOMETRY").withIndex(2).ofType(-2));
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

