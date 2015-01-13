package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SParent2 is a Querydsl querydsl type for SParent2
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SParent2 extends com.querydsl.sql.RelationalPathBase<SParent2> {

    private static final long serialVersionUID = 1859105463;

    public static final SParent2 Parent2 = new SParent2("Parent2");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SParent2> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SChild2> _fk783f9ab6c2dbacbc = createInvForeignKey(id, "parent_id");

    public SParent2(String variable) {
        super(SParent2.class, forVariable(variable), "", "Parent2");
        addMetadata();
    }

    public SParent2(String variable, String schema, String table) {
        super(SParent2.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SParent2(Path<? extends SParent2> path) {
        super(path.getType(), path.getMetadata(), "", "Parent2");
        addMetadata();
    }

    public SParent2(PathMetadata<?> metadata) {
        super(SParent2.class, metadata, "", "Parent2");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

