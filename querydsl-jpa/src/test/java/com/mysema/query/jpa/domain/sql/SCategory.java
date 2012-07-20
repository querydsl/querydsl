package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCategory is a Querydsl query type for SCategory
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCategory extends com.mysema.query.sql.RelationalPathBase<SCategory> {

    private static final long serialVersionUID = 673043695;

    public static final SCategory category = new SCategory("category_");

    public final StringPath categorydescription = createString("CATEGORYDESCRIPTION");

    public final StringPath categoryname = createString("CATEGORYNAME");

    public final NumberPath<Double> createdby = createNumber("CREATEDBY", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationdate = createDateTime("CREATIONDATE", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deletedate = createDateTime("DELETEDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedby = createNumber("DELETEDBY", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationdate = createDateTime("MODIFICATIONDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedby = createNumber("MODIFIEDBY", Double.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SUserprop_category> _userprop_category_childCategoriesIDFK = createInvForeignKey(id, "childCategories_ID");

    public final com.mysema.query.sql.ForeignKey<SCategory_category> _fkc4e60b832974945f = createInvForeignKey(id, "category__id");

    public final com.mysema.query.sql.ForeignKey<SCategory_category> _category_category_CategoryIDFK = createInvForeignKey(id, "Category_ID");

    public final com.mysema.query.sql.ForeignKey<SCategory_categoryprop> _category_categoryprop_CategoryIDFK = createInvForeignKey(id, "Category_ID");

    public final com.mysema.query.sql.ForeignKey<SCategory_category> _category_category_childCategoriesIDFK = createInvForeignKey(id, "childCategories_ID");

    public final com.mysema.query.sql.ForeignKey<SCategory_categoryprop> _fk8543a2802974945f = createInvForeignKey(id, "category__id");

    public SCategory(String variable) {
        super(SCategory.class, forVariable(variable), "null", "category_");
    }

    public SCategory(Path<? extends SCategory> path) {
        super(path.getType(), path.getMetadata(), "null", "category_");
    }

    public SCategory(PathMetadata<?> metadata) {
        super(SCategory.class, metadata, "null", "category_");
    }

}

