package com.querydsl.jpa.domain.sql;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.sql.ColumnMetadata;


/**
 * SShapes is a Querydsl query type for SShapes
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SShapes extends com.querydsl.sql.RelationalPathBase<SShapes> {

    private static final long serialVersionUID = 844563747;

    public static final SShapes shapes = new SShapes("SHAPES");

    public final SimplePath<byte[]> geometry = createSimple("geometry", byte[].class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SShapes> primary = createPrimaryKey(id);

    public SShapes(String variable) {
        super(SShapes.class, forVariable(variable), "", "SHAPES");
        addMetadata();
    }

    public SShapes(String variable, String schema, String table) {
        super(SShapes.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SShapes(Path<? extends SShapes> path) {
        super(path.getType(), path.getMetadata(), "", "SHAPES");
        addMetadata();
    }

    public SShapes(PathMetadata metadata) {
        super(SShapes.class, metadata, "", "SHAPES");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(geometry, ColumnMetadata.named("GEOMETRY").withIndex(2).ofType(-2));
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

