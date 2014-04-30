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
 * SWorld is a Querydsl query type for SWorld
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SWorld extends com.mysema.query.sql.RelationalPathBase<SWorld> {

    private static final long serialVersionUID = -107384511;

    public static final SWorld World = new SWorld("World");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SWorld> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SWorldMammal> _fk4070aeecd3538cf8 = createInvForeignKey(id, "World_id");

    public SWorld(String variable) {
        super(SWorld.class, forVariable(variable), "null", "World");
        addMetadata();
    }

    public SWorld(String variable, String schema, String table) {
        super(SWorld.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SWorld(Path<? extends SWorld> path) {
        super(path.getType(), path.getMetadata(), "null", "World");
        addMetadata();
    }

    public SWorld(PathMetadata<?> metadata) {
        super(SWorld.class, metadata, "null", "World");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

