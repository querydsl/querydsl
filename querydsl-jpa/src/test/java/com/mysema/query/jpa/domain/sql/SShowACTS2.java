package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SShowACTS is a Querydsl query type for SShowACTS
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SShowACTS2 extends com.mysema.query.sql.RelationalPathBase<SShowACTS2> {

    private static final long serialVersionUID = 283098767;

    public static final SShowACTS2 ShowACTS = new SShowACTS2("Show_ACTS");

    public final StringPath acts = createString("ACTS");

    public final StringPath actsKey = createString("ACTS_KEY");

    public final NumberPath<Long> showID = createNumber("Show_ID", Long.class);

    public final com.mysema.query.sql.ForeignKey<SShow> showACTSShowIDFK = createForeignKey(showID, "id");

    public SShowACTS2(String variable) {
        super(SShowACTS2.class, forVariable(variable), "null", "Show_ACTS");
    }

    public SShowACTS2(Path<? extends SShowACTS2> path) {
        super(path.getType(), path.getMetadata(), "null", "Show_ACTS");
    }

    public SShowACTS2(PathMetadata<?> metadata) {
        super(SShowACTS2.class, metadata, "null", "Show_ACTS");
    }

}

