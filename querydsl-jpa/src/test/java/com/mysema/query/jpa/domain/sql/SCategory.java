package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCategory is a Querydsl query type for SCategory
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCategory extends com.mysema.query.sql.RelationalPathBase<SCategory> {

    private static final long serialVersionUID = 673043695;

    public static final SCategory category = new SCategory("CATEGORY_");

    public final StringPath categorydescription = createString("CATEGORYDESCRIPTION");

    public final StringPath categoryname = createString("CATEGORYNAME");

    public final NumberPath<Double> createdby = createNumber("CREATEDBY", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationdate = createDateTime("CREATIONDATE", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deletedate = createDateTime("DELETEDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedby = createNumber("DELETEDBY", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationdate = createDateTime("MODIFICATIONDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedby = createNumber("MODIFIEDBY", Double.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory> sql120219232321330 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCategory_category> _fkc4e60b832974945f = createInvForeignKey(id, "CATEGORY__ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop_category> _fk851f48d37ab543e8 = createInvForeignKey(id, "CHILDCATEGORIES_ID");

    public final com.mysema.query.sql.ForeignKey<SCategory_categoryprop> _fk8543a2802974945f = createInvForeignKey(id, "CATEGORY__ID");

    public final com.mysema.query.sql.ForeignKey<SCategory_category> _fkc4e60b837ab543e8 = createInvForeignKey(id, "CHILDCATEGORIES_ID");

    public SCategory(String variable) {
        super(SCategory.class, forVariable(variable), "APP", "CATEGORY_");
    }

    public SCategory(Path<? extends SCategory> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CATEGORY_");
    }

    public SCategory(PathMetadata<?> metadata) {
        super(SCategory.class, metadata, "APP", "CATEGORY_");
    }

}

