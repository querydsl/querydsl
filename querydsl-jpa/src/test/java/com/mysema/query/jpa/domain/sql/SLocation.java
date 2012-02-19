package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SLocation is a Querydsl query type for SLocation
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SLocation extends com.mysema.query.sql.RelationalPathBase<SLocation> {

    private static final long serialVersionUID = -1771391066;

    public static final SLocation location = new SLocation("LOCATION_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SLocation> sql120219232326160 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SStore> _fkcad4239e8a55845c = createInvForeignKey(id, "LOCATION_ID");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable), "APP", "LOCATION_");
    }

    public SLocation(Path<? extends SLocation> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "LOCATION_");
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata, "APP", "LOCATION_");
    }

}

