package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStore is a Querydsl query type for SStore
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStore extends com.mysema.query.sql.RelationalPathBase<SStore> {

    private static final long serialVersionUID = -110932336;

    public static final SStore store = new SStore("store_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> locationId = createNumber("LOCATION_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SStore> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SLocation> store_LOCATIONIDFK = createForeignKey(locationId, "ID");

    public final com.mysema.query.sql.ForeignKey<SStore_customer> _store_customer_StoreIDFK = createInvForeignKey(id, "Store_ID");

    public final com.mysema.query.sql.ForeignKey<SStore_customer> _fk82ba2ce035d2d6bb = createInvForeignKey(id, "store__id");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable), "null", "store_");
    }

    public SStore(Path<? extends SStore> path) {
        super(path.getType(), path.getMetadata(), "null", "store_");
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata, "null", "store_");
    }

}

