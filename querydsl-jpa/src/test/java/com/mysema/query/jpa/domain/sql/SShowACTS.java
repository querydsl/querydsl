package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SShowACTS is a Querydsl query type for SShowACTS
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SShowACTS extends com.mysema.query.sql.RelationalPathBase<SShowACTS> {

    private static final long serialVersionUID = 283098767;

    public static final SShowACTS ShowACTS = new SShowACTS("Show_ACTS");

    public final StringPath acts = createString("ACTS");

    public final StringPath actsKey = createString("ACTS_KEY");

    public final NumberPath<Long> showID = createNumber("Show_ID", Long.class);

    public final com.mysema.query.sql.ForeignKey<SShow> showACTSShowIDFK = createForeignKey(showID, "id");

    public SShowACTS(String variable) {
        super(SShowACTS.class, forVariable(variable), "null", "Show_ACTS");
    }

    public SShowACTS(Path<? extends SShowACTS> path) {
        super(path.getType(), path.getMetadata(), "null", "Show_ACTS");
    }

    public SShowACTS(PathMetadata<?> metadata) {
        super(SShowACTS.class, metadata, "null", "Show_ACTS");
    }

}

