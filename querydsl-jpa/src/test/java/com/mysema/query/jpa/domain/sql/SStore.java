package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStore is a Querydsl query type for SStore
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SStore extends com.mysema.query.sql.RelationalPathBase<SStore> {

    private static final long serialVersionUID = -110932336;

    public static final SStore store = new SStore("STORE_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> locationId = createNumber("LOCATION_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SStore> sql120219232330110 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SLocation> fkcad4239e8a55845c = createForeignKey(locationId, "ID");

    public final com.mysema.query.sql.ForeignKey<SStore_customer> _fk82ba2ce035d2d6bb = createInvForeignKey(id, "STORE__ID");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable), "APP", "STORE_");
    }

    public SStore(Path<? extends SStore> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "STORE_");
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata, "APP", "STORE_");
    }

}

