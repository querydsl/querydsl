package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNamelistNames is a Querydsl query type for SNamelistNames
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SNamelistNames extends com.mysema.query.sql.RelationalPathBase<SNamelistNames> {

    private static final long serialVersionUID = -1853664818;

    public static final SNamelistNames namelistNames = new SNamelistNames("NAMELIST_NAMES");

    public final NumberPath<Long> namelistId = createNumber("NAMELIST_ID", Long.class);

    public final StringPath names = createString("NAMES");

    public final com.mysema.query.sql.ForeignKey<SNamelist> fkd6c82d7217b6c3fc = createForeignKey(namelistId, "ID");

    public SNamelistNames(String variable) {
        super(SNamelistNames.class, forVariable(variable), "APP", "NAMELIST_NAMES");
    }

    public SNamelistNames(Path<? extends SNamelistNames> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "NAMELIST_NAMES");
    }

    public SNamelistNames(PathMetadata<?> metadata) {
        super(SNamelistNames.class, metadata, "APP", "NAMELIST_NAMES");
    }

}

