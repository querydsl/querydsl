package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SUser2_userprop is a Querydsl query type for SUser2_userprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUser2_userprop extends com.mysema.query.sql.RelationalPathBase<SUser2_userprop> {

    private static final long serialVersionUID = -598341912;

    public static final SUser2_userprop user2_userprop_ = new SUser2_userprop("user2__userprop_");

    public final NumberPath<Long> propertiesId = createNumber("propertiesId", Long.class);

    public final NumberPath<Long> user2_id = createNumber("user2_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUser2_userprop> primary = createPrimaryKey(propertiesId, user2_id);

    public final com.mysema.query.sql.ForeignKey<SUser2> fk4611b46af21971a1 = createForeignKey(user2_id, "id");

    public final com.mysema.query.sql.ForeignKey<SUserprop> fk4611b46aa56541dd = createForeignKey(propertiesId, "id");

    public SUser2_userprop(String variable) {
        super(SUser2_userprop.class, forVariable(variable), "", "user2__userprop_");
        addMetadata();
    }

    public SUser2_userprop(String variable, String schema, String table) {
        super(SUser2_userprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUser2_userprop(Path<? extends SUser2_userprop> path) {
        super(path.getType(), path.getMetadata(), "", "user2__userprop_");
        addMetadata();
    }

    public SUser2_userprop(PathMetadata<?> metadata) {
        super(SUser2_userprop.class, metadata, "", "user2__userprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(propertiesId, ColumnMetadata.named("properties_id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(user2_id, ColumnMetadata.named("user2__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

