package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SCategoryprop is a Querydsl query type for SCategoryprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCategoryprop extends com.mysema.query.sql.RelationalPathBase<SCategoryprop> {

    private static final long serialVersionUID = -1013141043;

    public static final SCategoryprop categoryprop_ = new SCategoryprop("categoryprop_");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath propName = createString("propName");

    public final StringPath propValue = createString("propValue");

    public final com.mysema.query.sql.PrimaryKey<SCategoryprop> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SUserprop_categoryprop> _fke0fdb7d0fd94cd90 = createInvForeignKey(id, "properties_id");

    public final com.mysema.query.sql.ForeignKey<SCategory_categoryprop> _fk8543a280fd94cd90 = createInvForeignKey(id, "properties_id");

    public SCategoryprop(String variable) {
        super(SCategoryprop.class, forVariable(variable), "null", "categoryprop_");
        addMetadata();
    }

    public SCategoryprop(String variable, String schema, String table) {
        super(SCategoryprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCategoryprop(Path<? extends SCategoryprop> path) {
        super(path.getType(), path.getMetadata(), "null", "categoryprop_");
        addMetadata();
    }

    public SCategoryprop(PathMetadata<?> metadata) {
        super(SCategoryprop.class, metadata, "null", "categoryprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(categoryId, ColumnMetadata.named("categoryId").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(propName, ColumnMetadata.named("propName").withIndex(3).ofType(12).withSize(255));
        addMetadata(propValue, ColumnMetadata.named("propValue").withIndex(4).ofType(12).withSize(255));
    }

}

