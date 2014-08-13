package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SCategory_categoryprop is a Querydsl query type for SCategory_categoryprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCategory_categoryprop extends com.mysema.query.sql.RelationalPathBase<SCategory_categoryprop> {

    private static final long serialVersionUID = -1348316210;

    public static final SCategory_categoryprop category_categoryprop_ = new SCategory_categoryprop("category__categoryprop_");

    public final NumberPath<Long> category_id = createNumber("category_id", Long.class);

    public final NumberPath<Long> propertiesId = createNumber("propertiesId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCategory_categoryprop> primary = createPrimaryKey(category_id, propertiesId);

    public final com.mysema.query.sql.ForeignKey<SCategoryprop> fk8543a280fd94cd90 = createForeignKey(propertiesId, "id");

    public final com.mysema.query.sql.ForeignKey<SCategory> fk8543a2802974945f = createForeignKey(category_id, "id");

    public SCategory_categoryprop(String variable) {
        super(SCategory_categoryprop.class, forVariable(variable), "", "category__categoryprop_");
        addMetadata();
    }

    public SCategory_categoryprop(String variable, String schema, String table) {
        super(SCategory_categoryprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCategory_categoryprop(Path<? extends SCategory_categoryprop> path) {
        super(path.getType(), path.getMetadata(), "", "category__categoryprop_");
        addMetadata();
    }

    public SCategory_categoryprop(PathMetadata<?> metadata) {
        super(SCategory_categoryprop.class, metadata, "", "category__categoryprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(category_id, ColumnMetadata.named("category__id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(propertiesId, ColumnMetadata.named("properties_id").withIndex(2).ofType(-5).withSize(19).notNull());
    }

}

