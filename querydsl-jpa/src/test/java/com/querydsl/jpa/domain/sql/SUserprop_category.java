package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SUserprop_category is a Querydsl querydsl type for SUserprop_category
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SUserprop_category extends com.querydsl.sql.RelationalPathBase<SUserprop_category> {

    private static final long serialVersionUID = 301952129;

    public static final SUserprop_category userprop_category_ = new SUserprop_category("userprop__category_");

    public final NumberPath<Long> childCategoriesId = createNumber("childCategoriesId", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("userprop_id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SUserprop_category> primary = createPrimaryKey(childCategoriesId, userprop_id);

    public final com.querydsl.sql.ForeignKey<SUserprop> fk851f48d3904c19df = createForeignKey(userprop_id, "id");

    public final com.querydsl.sql.ForeignKey<SCategory> fk851f48d37ab543e8 = createForeignKey(childCategoriesId, "id");

    public SUserprop_category(String variable) {
        super(SUserprop_category.class, forVariable(variable), "", "userprop__category_");
        addMetadata();
    }

    public SUserprop_category(String variable, String schema, String table) {
        super(SUserprop_category.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUserprop_category(Path<? extends SUserprop_category> path) {
        super(path.getType(), path.getMetadata(), "", "userprop__category_");
        addMetadata();
    }

    public SUserprop_category(PathMetadata<?> metadata) {
        super(SUserprop_category.class, metadata, "", "userprop__category_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(childCategoriesId, ColumnMetadata.named("childCategories_id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(userprop_id, ColumnMetadata.named("userprop__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

