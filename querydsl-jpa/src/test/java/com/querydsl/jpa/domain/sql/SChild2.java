package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SChild2 is a Querydsl querydsl type for SChild2
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SChild2 extends com.querydsl.sql.RelationalPathBase<SChild2> {

    private static final long serialVersionUID = 386731719;

    public static final SChild2 Child2 = new SChild2("Child2");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> parentId = createNumber("parentId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SChild2> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SParent2> fk783f9ab6c2dbacbc = createForeignKey(parentId, "id");

    public SChild2(String variable) {
        super(SChild2.class, forVariable(variable), "", "Child2");
        addMetadata();
    }

    public SChild2(String variable, String schema, String table) {
        super(SChild2.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SChild2(Path<? extends SChild2> path) {
        super(path.getType(), path.getMetadata(), "", "Child2");
        addMetadata();
    }

    public SChild2(PathMetadata<?> metadata) {
        super(SChild2.class, metadata, "", "Child2");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(parentId, ColumnMetadata.named("parent_id").withIndex(2).ofType(4).withSize(10));
    }

}

