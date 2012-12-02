package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNamelist is a Querydsl query type for SNamelist
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNamelist extends com.mysema.query.sql.RelationalPathBase<SNamelist> {

    private static final long serialVersionUID = -1831139942;

    public static final SNamelist namelist = new SNamelist("namelist_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SNamelist> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SNameListNAMES2> _nameListNAMESNameListIDFK = createInvForeignKey(id, "NameList_ID");

    public final com.mysema.query.sql.ForeignKey<SNameListNames> _fkd6c82d7217b6c3fc = createInvForeignKey(id, "NameList_id");

    public SNamelist(String variable) {
        super(SNamelist.class, forVariable(variable), "null", "namelist_");
    }

    public SNamelist(Path<? extends SNamelist> path) {
        super(path.getType(), path.getMetadata(), "null", "namelist_");
    }

    public SNamelist(PathMetadata<?> metadata) {
        super(SNamelist.class, metadata, "null", "namelist_");
    }

}

