package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCategory_category is a Querydsl query type for SCategory_category
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCategory_category extends com.mysema.query.sql.RelationalPathBase<SCategory_category> {

    private static final long serialVersionUID = -1826015666;

    public static final SCategory_category category_category = new SCategory_category("category__category_");

    public final NumberPath<Long> category_id = createNumber("category__id", Long.class);

    public final NumberPath<Long> categoryID = createNumber("Category_ID", Long.class);

    public final NumberPath<Long> childCategoriesID = createNumber("childCategories_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory_category> primary = createPrimaryKey(categoryID, childCategoriesID);

    public final com.mysema.query.sql.ForeignKey<SCategory> fkc4e60b832974945f = createForeignKey(category_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> category_category_CategoryIDFK = createForeignKey(categoryID, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> category_category_childCategoriesIDFK = createForeignKey(childCategoriesID, "ID");

    public SCategory_category(String variable) {
        super(SCategory_category.class, forVariable(variable), "null", "category__category_");
    }

    public SCategory_category(Path<? extends SCategory_category> path) {
        super(path.getType(), path.getMetadata(), "null", "category__category_");
    }

    public SCategory_category(PathMetadata<?> metadata) {
        super(SCategory_category.class, metadata, "null", "category__category_");
    }

}

