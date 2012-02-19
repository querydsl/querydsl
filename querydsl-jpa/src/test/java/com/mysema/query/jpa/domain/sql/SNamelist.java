package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNamelist is a Querydsl query type for SNamelist
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SNamelist extends com.mysema.query.sql.RelationalPathBase<SNamelist> {

    private static final long serialVersionUID = -1831139942;

    public static final SNamelist namelist = new SNamelist("NAMELIST_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SNamelist> sql120219232326820 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SNamelistNames> _fkd6c82d7217b6c3fc = createInvForeignKey(id, "NAMELIST_ID");

    public SNamelist(String variable) {
        super(SNamelist.class, forVariable(variable), "APP", "NAMELIST_");
    }

    public SNamelist(Path<? extends SNamelist> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "NAMELIST_");
    }

    public SNamelist(PathMetadata<?> metadata) {
        super(SNamelist.class, metadata, "APP", "NAMELIST_");
    }

}

