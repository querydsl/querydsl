package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SShowActs is a Querydsl query type for SShowActs
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SShowActs extends com.mysema.query.sql.RelationalPathBase<SShowActs> {

    private static final long serialVersionUID = 283130543;

    public static final SShowActs ShowActs = new SShowActs("Show_acts");

    public final StringPath acts = createString("acts");

    public final StringPath actsKEY = createString("actsKEY");

    public final NumberPath<Long> showId = createNumber("showId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SShowActs> primary = createPrimaryKey(showId, actsKEY);

    public final com.mysema.query.sql.ForeignKey<SShow> fk5f6ee03ab40105c = createForeignKey(showId, "id");

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable), "", "Show_acts");
        addMetadata();
    }

    public SShowActs(String variable, String schema, String table) {
        super(SShowActs.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SShowActs(Path<? extends SShowActs> path) {
        super(path.getType(), path.getMetadata(), "", "Show_acts");
        addMetadata();
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata, "", "Show_acts");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(acts, ColumnMetadata.named("acts").withIndex(2).ofType(12).withSize(255));
        addMetadata(actsKEY, ColumnMetadata.named("acts_KEY").withIndex(3).ofType(12).withSize(255).notNull());
        addMetadata(showId, ColumnMetadata.named("Show_id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

