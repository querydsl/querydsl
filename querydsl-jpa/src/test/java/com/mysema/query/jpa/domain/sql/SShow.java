package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SShow is a Querydsl query type for SShow
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SShow extends com.mysema.query.sql.RelationalPathBase<SShow> {

    private static final long serialVersionUID = -1389063314;

    public static final SShow show = new SShow("show_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SShow> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SShowACTS2> _showACTSShowIDFK = createInvForeignKey(id, "Show_ID");

    public final com.mysema.query.sql.ForeignKey<SShowActs> _fk5f6ee03ab40105c = createInvForeignKey(id, "Show_id");

    public SShow(String variable) {
        super(SShow.class, forVariable(variable), "null", "show_");
    }

    public SShow(Path<? extends SShow> path) {
        super(path.getType(), path.getMetadata(), "null", "show_");
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata, "null", "show_");
    }

}

