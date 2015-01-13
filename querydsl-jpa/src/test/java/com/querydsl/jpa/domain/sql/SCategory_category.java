package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCategory_category_ is a Querydsl querydsl type for SCategory_category_
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCategory_category extends com.querydsl.sql.RelationalPathBase<SCategory_category> {

    private static final long serialVersionUID = -771910703;

    public static final SCategory_category category_category_ = new SCategory_category("category__category_");

    public final NumberPath<Long> childId = createNumber("childId", Long.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SCategory_category> primary = createPrimaryKey(childId, parentId);

    public final com.querydsl.sql.ForeignKey<SCategory> fkc4e60b83561378ab = createForeignKey(parentId, "id");

    public final com.querydsl.sql.ForeignKey<SCategory> fkc4e60b833c83109d = createForeignKey(childId, "id");

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

