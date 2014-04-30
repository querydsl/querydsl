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
 * SParent2 is a Querydsl query type for SParent2
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SParent2 extends com.mysema.query.sql.RelationalPathBase<SParent2> {

    private static final long serialVersionUID = 1859105463;

    public static final SParent2 Parent2 = new SParent2("Parent2");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SParent2> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SChild2> _fk783f9ab6c2dbacbc = createInvForeignKey(id, "parent_id");

    public SParent2(String variable) {
        super(SParent2.class, forVariable(variable), "null", "Parent2");
        addMetadata();
    }

    public SParent2(String variable, String schema, String table) {
        super(SParent2.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SParent2(Path<? extends SParent2> path) {
        super(path.getType(), path.getMetadata(), "null", "Parent2");
        addMetadata();
    }

    public SParent2(PathMetadata<?> metadata) {
        super(SParent2.class, metadata, "null", "Parent2");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

