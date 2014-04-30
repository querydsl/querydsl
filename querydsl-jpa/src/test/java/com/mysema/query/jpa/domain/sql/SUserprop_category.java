package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SUserprop_category is a Querydsl query type for SUserprop_category
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUserprop_category extends com.mysema.query.sql.RelationalPathBase<SUserprop_category> {

    private static final long serialVersionUID = 301952129;

    public static final SUserprop_category userprop_category_ = new SUserprop_category("userprop__category_");

    public final NumberPath<Long> childCategoriesId = createNumber("childCategoriesId", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("userprop_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop_category> primary = createPrimaryKey(childCategoriesId, userprop_id);

    public final com.mysema.query.sql.ForeignKey<SUserprop> fk851f48d3904c19df = createForeignKey(userprop_id, "id");

    public final com.mysema.query.sql.ForeignKey<SCategory> fk851f48d37ab543e8 = createForeignKey(childCategoriesId, "id");

    public SUserprop_category(String variable) {
        super(SUserprop_category.class, forVariable(variable), "null", "userprop__category_");
        addMetadata();
    }

    public SUserprop_category(String variable, String schema, String table) {
        super(SUserprop_category.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUserprop_category(Path<? extends SUserprop_category> path) {
        super(path.getType(), path.getMetadata(), "null", "userprop__category_");
        addMetadata();
    }

    public SUserprop_category(PathMetadata<?> metadata) {
        super(SUserprop_category.class, metadata, "null", "userprop__category_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(childCategoriesId, ColumnMetadata.named("childCategories_id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(userprop_id, ColumnMetadata.named("userprop__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

