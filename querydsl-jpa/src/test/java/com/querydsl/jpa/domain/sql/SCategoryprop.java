package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCategoryprop is a Querydsl querydsl type for SCategoryprop
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCategoryprop extends com.querydsl.sql.RelationalPathBase<SCategoryprop> {

    private static final long serialVersionUID = -1013141043;

    public static final SCategoryprop categoryprop_ = new SCategoryprop("categoryprop_");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath propName = createString("propName");

    public final StringPath propValue = createString("propValue");

    public final com.querydsl.sql.PrimaryKey<SCategoryprop> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SUserprop_categoryprop> _fke0fdb7d0fd94cd90 = createInvForeignKey(id, "properties_id");

    public final com.querydsl.sql.ForeignKey<SCategory_categoryprop> _fk8543a280fd94cd90 = createInvForeignKey(id, "properties_id");

    public SCategoryprop(String variable) {
        super(SCategoryprop.class, forVariable(variable), "", "categoryprop_");
        addMetadata();
    }

    public SCategoryprop(String variable, String schema, String table) {
        super(SCategoryprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCategoryprop(Path<? extends SCategoryprop> path) {
        super(path.getType(), path.getMetadata(), "", "categoryprop_");
        addMetadata();
    }

    public SCategoryprop(PathMetadata<?> metadata) {
        super(SCategoryprop.class, metadata, "", "categoryprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(categoryId, ColumnMetadata.named("categoryId").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(propName, ColumnMetadata.named("propName").withIndex(3).ofType(12).withSize(255));
        addMetadata(propValue, ColumnMetadata.named("propValue").withIndex(4).ofType(12).withSize(255));
    }

}

