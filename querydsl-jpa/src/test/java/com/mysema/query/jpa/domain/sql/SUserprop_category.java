package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUserprop_category is a Querydsl query type for SUserprop_category
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUserprop_category extends com.mysema.query.sql.RelationalPathBase<SUserprop_category> {

    private static final long serialVersionUID = 1949403038;

    public static final SUserprop_category userprop_category = new SUserprop_category("userprop__category_");

    public final NumberPath<Long> childCategoriesID = createNumber("childCategories_ID", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("userprop__id", Long.class);

    public final NumberPath<Long> userPropID = createNumber("UserProp_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop_category> primary = createPrimaryKey(userPropID, childCategoriesID);

    public final com.mysema.query.sql.ForeignKey<SUserprop> userprop_category_UserPropIDFK = createForeignKey(userPropID, "ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop> fk851f48d3904c19df = createForeignKey(userprop_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> userprop_category_childCategoriesIDFK = createForeignKey(childCategoriesID, "ID");

    public SUserprop_category(String variable) {
        super(SUserprop_category.class, forVariable(variable), "null", "userprop__category_");
    }

    public SUserprop_category(Path<? extends SUserprop_category> path) {
        super(path.getType(), path.getMetadata(), "null", "userprop__category_");
    }

    public SUserprop_category(PathMetadata<?> metadata) {
        super(SUserprop_category.class, metadata, "null", "userprop__category_");
    }

}

