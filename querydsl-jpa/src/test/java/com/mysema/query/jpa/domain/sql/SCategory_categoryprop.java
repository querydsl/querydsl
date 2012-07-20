package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCategory_categoryprop is a Querydsl query type for SCategory_categoryprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCategory_categoryprop extends com.mysema.query.sql.RelationalPathBase<SCategory_categoryprop> {

    private static final long serialVersionUID = -736230735;

    public static final SCategory_categoryprop category_categoryprop = new SCategory_categoryprop("category__categoryprop_");

    public final NumberPath<Long> category_id = createNumber("category__id", Long.class);

    public final NumberPath<Long> categoryID = createNumber("Category_ID", Long.class);

    public final NumberPath<Long> propertiesID = createNumber("properties_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory_categoryprop> primary = createPrimaryKey(categoryID, propertiesID);

    public final com.mysema.query.sql.ForeignKey<SCategoryprop> category_categoryprop_propertiesIDFK = createForeignKey(propertiesID, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> category_categoryprop_CategoryIDFK = createForeignKey(categoryID, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> fk8543a2802974945f = createForeignKey(category_id, "ID");

    public SCategory_categoryprop(String variable) {
        super(SCategory_categoryprop.class, forVariable(variable), "null", "category__categoryprop_");
    }

    public SCategory_categoryprop(Path<? extends SCategory_categoryprop> path) {
        super(path.getType(), path.getMetadata(), "null", "category__categoryprop_");
    }

    public SCategory_categoryprop(PathMetadata<?> metadata) {
        super(SCategory_categoryprop.class, metadata, "null", "category__categoryprop_");
    }

}

