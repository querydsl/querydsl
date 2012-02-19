package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCategory_category is a Querydsl query type for SCategory_category
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCategory_category extends com.mysema.query.sql.RelationalPathBase<SCategory_category> {

    private static final long serialVersionUID = -1826015666;

    public static final SCategory_category category_category = new SCategory_category("CATEGORY__CATEGORY_");

    public final NumberPath<Long> category_id = createNumber("CATEGORY__ID", Long.class);

    public final NumberPath<Long> childcategoriesId = createNumber("CHILDCATEGORIES_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory_category> sql120219232321530 = createPrimaryKey(category_id, childcategoriesId);

    public final com.mysema.query.sql.ForeignKey<SCategory> fkc4e60b832974945f = createForeignKey(category_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> fkc4e60b837ab543e8 = createForeignKey(childcategoriesId, "ID");

    public SCategory_category(String variable) {
        super(SCategory_category.class, forVariable(variable), "APP", "CATEGORY__CATEGORY_");
    }

    public SCategory_category(Path<? extends SCategory_category> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CATEGORY__CATEGORY_");
    }

    public SCategory_category(PathMetadata<?> metadata) {
        super(SCategory_category.class, metadata, "APP", "CATEGORY__CATEGORY_");
    }

}

