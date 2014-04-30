package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SStore is a Querydsl query type for SStore
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStore extends com.mysema.query.sql.RelationalPathBase<SStore> {

    private static final long serialVersionUID = 856064975;

    public static final SStore store_ = new SStore("store_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> locationId = createNumber("locationId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SStore> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SLocation> fkcad4239e8a55845c = createForeignKey(locationId, "id");

    public final com.mysema.query.sql.ForeignKey<SStore_customer> _fk82ba2ce035d2d6bb = createInvForeignKey(id, "store__id");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable), "null", "store_");
        addMetadata();
    }

    public SStore(String variable, String schema, String table) {
        super(SStore.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SStore(Path<? extends SStore> path) {
        super(path.getType(), path.getMetadata(), "null", "store_");
        addMetadata();
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata, "null", "store_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(locationId, ColumnMetadata.named("location_id").withIndex(2).ofType(-5).withSize(19));
    }

}

