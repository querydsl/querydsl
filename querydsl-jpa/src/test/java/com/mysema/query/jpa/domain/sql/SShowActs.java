package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SShowActs is a Querydsl query type for SShowActs
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SShowActs extends com.mysema.query.sql.RelationalPathBase<SShowActs> {

    private static final long serialVersionUID = 283130543;

    public static final SShowActs ShowActs = new SShowActs("Show_acts");

    public final StringPath acts = createString("acts");

    public final StringPath actsKEY = createString("acts_KEY");

    public final NumberPath<Long> showId = createNumber("Show_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SShowActs> primary = createPrimaryKey(showId, actsKEY);

    public final com.mysema.query.sql.ForeignKey<SShow> fk5f6ee03ab40105c = createForeignKey(showId, "id");

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable), "null", "Show_acts");
    }

    public SShowActs(Path<? extends SShowActs> path) {
        super(path.getType(), path.getMetadata(), "null", "Show_acts");
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata, "null", "Show_acts");
    }

}

