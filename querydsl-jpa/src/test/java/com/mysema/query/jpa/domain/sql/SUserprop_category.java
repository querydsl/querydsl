package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUserprop_category is a Querydsl query type for SUserprop_category
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SUserprop_category extends com.mysema.query.sql.RelationalPathBase<SUserprop_category> {

    private static final long serialVersionUID = 1949403038;

    public static final SUserprop_category userprop_category = new SUserprop_category("USERPROP__CATEGORY_");

    public final NumberPath<Long> childcategoriesId = createNumber("CHILDCATEGORIES_ID", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("USERPROP__ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop_category> sql120219232331470 = createPrimaryKey(childcategoriesId, userprop_id);

    public final com.mysema.query.sql.ForeignKey<SUserprop> fk851f48d3904c19df = createForeignKey(userprop_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> fk851f48d37ab543e8 = createForeignKey(childcategoriesId, "ID");

    public SUserprop_category(String variable) {
        super(SUserprop_category.class, forVariable(variable), "APP", "USERPROP__CATEGORY_");
    }

    public SUserprop_category(Path<? extends SUserprop_category> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "USERPROP__CATEGORY_");
    }

    public SUserprop_category(PathMetadata<?> metadata) {
        super(SUserprop_category.class, metadata, "APP", "USERPROP__CATEGORY_");
    }

}

