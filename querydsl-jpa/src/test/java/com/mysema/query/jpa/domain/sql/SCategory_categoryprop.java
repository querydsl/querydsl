package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCategory_categoryprop is a Querydsl query type for SCategory_categoryprop
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCategory_categoryprop extends com.mysema.query.sql.RelationalPathBase<SCategory_categoryprop> {

    private static final long serialVersionUID = -736230735;

    public static final SCategory_categoryprop category_categoryprop = new SCategory_categoryprop("CATEGORY__CATEGORYPROP_");

    public final NumberPath<Long> category_id = createNumber("CATEGORY__ID", Long.class);

    public final NumberPath<Long> propertiesId = createNumber("PROPERTIES_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory_categoryprop> sql120219232321860 = createPrimaryKey(category_id, propertiesId);

    public final com.mysema.query.sql.ForeignKey<SCategoryprop> fk8543a280fd94cd90 = createForeignKey(propertiesId, "ID");

    public final com.mysema.query.sql.ForeignKey<SCategory> fk8543a2802974945f = createForeignKey(category_id, "ID");

    public SCategory_categoryprop(String variable) {
        super(SCategory_categoryprop.class, forVariable(variable), "APP", "CATEGORY__CATEGORYPROP_");
    }

    public SCategory_categoryprop(Path<? extends SCategory_categoryprop> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CATEGORY__CATEGORYPROP_");
    }

    public SCategory_categoryprop(PathMetadata<?> metadata) {
        super(SCategory_categoryprop.class, metadata, "APP", "CATEGORY__CATEGORYPROP_");
    }

}

