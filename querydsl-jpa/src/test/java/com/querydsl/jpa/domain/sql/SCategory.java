package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DateTimePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCategory is a Querydsl querydsl type for SCategory
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCategory extends com.querydsl.sql.RelationalPathBase<SCategory> {

    private static final long serialVersionUID = -610481840;

    public static final SCategory category_ = new SCategory("category_");

    public final StringPath categoryDescription = createString("categoryDescription");

    public final StringPath categoryName = createString("categoryName");

    public final NumberPath<Double> createdBy = createNumber("createdBy", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationDate = createDateTime("creationDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deleteDate = createDateTime("deleteDate", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedBy = createNumber("deletedBy", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationDate = createDateTime("modificationDate", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedBy = createNumber("modifiedBy", Double.class);

    public final com.querydsl.sql.PrimaryKey<SCategory> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SCategory_category> _fkc4e60b83561378ab = createInvForeignKey(id, "parentId");

    public final com.querydsl.sql.ForeignKey<SUserprop_category> _fk851f48d37ab543e8 = createInvForeignKey(id, "childCategories_id");

    public final com.querydsl.sql.ForeignKey<SCategory_categoryprop> _fk8543a2802974945f = createInvForeignKey(id, "category__id");

    public final com.querydsl.sql.ForeignKey<SCategory_category> _fkc4e60b833c83109d = createInvForeignKey(id, "childId");

    public SCategory(String variable) {
        super(SCategory.class, forVariable(variable), "", "category_");
        addMetadata();
    }

    public SCategory(String variable, String schema, String table) {
        super(SCategory.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCategory(Path<? extends SCategory> path) {
        super(path.getType(), path.getMetadata(), "", "category_");
        addMetadata();
    }

    public SCategory(PathMetadata<?> metadata) {
        super(SCategory.class, metadata, "", "category_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(categoryDescription, ColumnMetadata.named("categoryDescription").withIndex(2).ofType(12).withSize(255));
        addMetadata(categoryName, ColumnMetadata.named("categoryName").withIndex(3).ofType(12).withSize(255));
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(4).ofType(8).withSize(22).notNull());
        addMetadata(creationDate, ColumnMetadata.named("creationDate").withIndex(5).ofType(93).withSize(19));
        addMetadata(deleteDate, ColumnMetadata.named("deleteDate").withIndex(6).ofType(93).withSize(19));
        addMetadata(deletedBy, ColumnMetadata.named("deletedBy").withIndex(7).ofType(8).withSize(22).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(modificationDate, ColumnMetadata.named("modificationDate").withIndex(8).ofType(93).withSize(19));
        addMetadata(modifiedBy, ColumnMetadata.named("modifiedBy").withIndex(9).ofType(8).withSize(22).notNull());
    }

}

