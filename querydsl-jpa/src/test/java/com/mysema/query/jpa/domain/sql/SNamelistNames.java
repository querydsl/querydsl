package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNamelistNames is a Querydsl query type for SNamelistNames
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNamelistNames extends com.mysema.query.sql.RelationalPathBase<SNamelistNames> {

    private static final long serialVersionUID = -1853664818;

    public static final SNamelistNames namelistNames = new SNamelistNames("namelist_names");

    public SNamelistNames(String variable) {
        super(SNamelistNames.class, forVariable(variable), "app", "namelist_names");
    }

    public SNamelistNames(Path<? extends SNamelistNames> path) {
        super(path.getType(), path.getMetadata(), "app", "namelist_names");
    }

    public SNamelistNames(PathMetadata<?> metadata) {
        super(SNamelistNames.class, metadata, "app", "namelist_names");
    }

}

