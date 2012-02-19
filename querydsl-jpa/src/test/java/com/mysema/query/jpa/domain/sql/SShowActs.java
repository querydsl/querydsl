package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SShowActs is a Querydsl query type for SShowActs
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SShowActs extends com.mysema.query.sql.RelationalPathBase<SShowActs> {

    private static final long serialVersionUID = 283130543;

    public static final SShowActs showActs = new SShowActs("SHOW_ACTS");

    public final StringPath acts = createString("ACTS");

    public final StringPath actsKey = createString("ACTS_KEY");

    public final NumberPath<Long> showId = createNumber("SHOW_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SShowActs> sql120219232319030 = createPrimaryKey(actsKey, showId);

    public final com.mysema.query.sql.ForeignKey<SShow> fk5f6ee03ab40105c = createForeignKey(showId, "ID");

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable), "APP", "SHOW_ACTS");
    }

    public SShowActs(Path<? extends SShowActs> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "SHOW_ACTS");
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata, "APP", "SHOW_ACTS");
    }

}

