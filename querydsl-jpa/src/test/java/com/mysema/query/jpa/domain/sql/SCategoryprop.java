package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCategoryprop is a Querydsl query type for SCategoryprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCategoryprop extends com.mysema.query.sql.RelationalPathBase<SCategoryprop> {

    private static final long serialVersionUID = -1972344622;

    public static final SCategoryprop categoryprop = new SCategoryprop("categoryprop_");

    public final NumberPath<Long> categoryid = createNumber("CATEGORYID", Long.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath propname = createString("PROPNAME");

    public final StringPath propvalue = createString("PROPVALUE");

    public final com.mysema.query.sql.PrimaryKey<SCategoryprop> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SUserprop_categoryprop> _userprop_categoryprop_propertiesIDFK = createInvForeignKey(id, "properties_ID");

    public final com.mysema.query.sql.ForeignKey<SCategory_categoryprop> _category_categoryprop_propertiesIDFK = createInvForeignKey(id, "properties_ID");

    public SCategoryprop(String variable) {
        super(SCategoryprop.class, forVariable(variable), "null", "categoryprop_");
    }

    public SCategoryprop(Path<? extends SCategoryprop> path) {
        super(path.getType(), path.getMetadata(), "null", "categoryprop_");
    }

    public SCategoryprop(PathMetadata<?> metadata) {
        super(SCategoryprop.class, metadata, "null", "categoryprop_");
    }

}

