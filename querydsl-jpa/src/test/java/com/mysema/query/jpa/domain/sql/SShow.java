package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SShow is a Querydsl query type for SShow
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SShow extends com.mysema.query.sql.RelationalPathBase<SShow> {

    private static final long serialVersionUID = -111289679;

    public static final SShow show_ = new SShow("show_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SShow> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SShowActs> _fk5f6ee03ab40105c = createInvForeignKey(id, "Show_id");

    public SShow(String variable) {
        super(SShow.class, forVariable(variable), "", "show_");
        addMetadata();
    }

    public SShow(String variable, String schema, String table) {
        super(SShow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SShow(Path<? extends SShow> path) {
        super(path.getType(), path.getMetadata(), "", "show_");
        addMetadata();
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata, "", "show_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

