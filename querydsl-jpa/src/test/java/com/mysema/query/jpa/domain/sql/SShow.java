package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SShow is a Querydsl query type for SShow
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SShow extends com.mysema.query.sql.RelationalPathBase<SShow> {

    private static final long serialVersionUID = -1389063314;

    public static final SShow show = new SShow("SHOW_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SShow> sql120219232329130 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SShowActs> _fk5f6ee03ab40105c = createInvForeignKey(id, "SHOW_ID");

    public SShow(String variable) {
        super(SShow.class, forVariable(variable), "APP", "SHOW_");
    }

    public SShow(Path<? extends SShow> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "SHOW_");
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata, "APP", "SHOW_");
    }

}

