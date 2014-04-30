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
 * SChild2 is a Querydsl query type for SChild2
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SChild2 extends com.mysema.query.sql.RelationalPathBase<SChild2> {

    private static final long serialVersionUID = 386731719;

    public static final SChild2 Child2 = new SChild2("Child2");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> parentId = createNumber("parentId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SChild2> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SParent2> fk783f9ab6c2dbacbc = createForeignKey(parentId, "id");

    public SChild2(String variable) {
        super(SChild2.class, forVariable(variable), "null", "Child2");
        addMetadata();
    }

    public SChild2(String variable, String schema, String table) {
        super(SChild2.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SChild2(Path<? extends SChild2> path) {
        super(path.getType(), path.getMetadata(), "null", "Child2");
        addMetadata();
    }

    public SChild2(PathMetadata<?> metadata) {
        super(SChild2.class, metadata, "null", "Child2");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(parentId, ColumnMetadata.named("parent_id").withIndex(2).ofType(4).withSize(10));
    }

}

