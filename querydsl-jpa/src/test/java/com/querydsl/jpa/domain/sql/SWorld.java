package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SWorld is a Querydsl querydsl type for SWorld
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SWorld extends com.querydsl.sql.RelationalPathBase<SWorld> {

    private static final long serialVersionUID = -107384511;

    public static final SWorld World = new SWorld("World");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SWorld> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SWorldMammal> _fk4070aeecd3538cf8 = createInvForeignKey(id, "World_id");

    public SWorld(String variable) {
        super(SWorld.class, forVariable(variable), "", "World");
        addMetadata();
    }

    public SWorld(String variable, String schema, String table) {
        super(SWorld.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SWorld(Path<? extends SWorld> path) {
        super(path.getType(), path.getMetadata(), "", "World");
        addMetadata();
    }

    public SWorld(PathMetadata<?> metadata) {
        super(SWorld.class, metadata, "", "World");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

