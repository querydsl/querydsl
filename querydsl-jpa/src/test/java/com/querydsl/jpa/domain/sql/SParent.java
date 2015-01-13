package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SParent is a Querydsl querydsl type for SParent
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SParent extends com.querydsl.sql.RelationalPathBase<SParent> {

    private static final long serialVersionUID = 1859105508;

    public static final SParent parent_ = new SParent("parent_");

    public final StringPath childName = createString("childName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<SParent> primary = createPrimaryKey(id);

    public SParent(String variable) {
        super(SParent.class, forVariable(variable), "", "parent_");
        addMetadata();
    }

    public SParent(String variable, String schema, String table) {
        super(SParent.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SParent(Path<? extends SParent> path) {
        super(path.getType(), path.getMetadata(), "", "parent_");
        addMetadata();
    }

    public SParent(PathMetadata<?> metadata) {
        super(SParent.class, metadata, "", "parent_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(childName, ColumnMetadata.named("childName").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(12).withSize(255));
    }

}

