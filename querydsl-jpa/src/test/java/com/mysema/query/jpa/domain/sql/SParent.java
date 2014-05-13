package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SParent is a Querydsl query type for SParent
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SParent extends com.mysema.query.sql.RelationalPathBase<SParent> {

    private static final long serialVersionUID = 1859105508;

    public static final SParent parent_ = new SParent("parent_");

    public final StringPath childName = createString("childName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SParent> primary = createPrimaryKey(id);

    public SParent(String variable) {
        super(SParent.class, forVariable(variable), "null", "parent_");
        addMetadata();
    }

    public SParent(String variable, String schema, String table) {
        super(SParent.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SParent(Path<? extends SParent> path) {
        super(path.getType(), path.getMetadata(), "null", "parent_");
        addMetadata();
    }

    public SParent(PathMetadata<?> metadata) {
        super(SParent.class, metadata, "null", "parent_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(childName, ColumnMetadata.named("childName").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(12).withSize(255));
    }

}

