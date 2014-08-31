package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SCategory_category_ is a Querydsl query type for SCategory_category_
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCategory_category extends com.mysema.query.sql.RelationalPathBase<SCategory_category> {

    private static final long serialVersionUID = -771910703;

    public static final SCategory_category category_category_ = new SCategory_category("category__category_");

    public final NumberPath<Long> childId = createNumber("childId", Long.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory_category> primary = createPrimaryKey(childId, parentId);

    public final com.mysema.query.sql.ForeignKey<SCategory> fkc4e60b83561378ab = createForeignKey(parentId, "id");

    public final com.mysema.query.sql.ForeignKey<SCategory> fkc4e60b833c83109d = createForeignKey(childId, "id");

    public SCategory_category(String variable) {
        super(SCategory_category.class, forVariable(variable), "", "category__category_");
        addMetadata();
    }

    public SCategory_category(String variable, String schema, String table) {
        super(SCategory_category.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCategory_category(Path<? extends SCategory_category> path) {
        super(path.getType(), path.getMetadata(), "", "category__category_");
        addMetadata();
    }

    public SCategory_category(PathMetadata<?> metadata) {
        super(SCategory_category.class, metadata, "", "category__category_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(childId, ColumnMetadata.named("childId").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(parentId, ColumnMetadata.named("parentId").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

