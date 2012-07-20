package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPersonid is a Querydsl query type for SPersonid
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPersonid extends com.mysema.query.sql.RelationalPathBase<SPersonid> {

    private static final long serialVersionUID = 1065697057;

    public static final SPersonid personid = new SPersonid("personid_");

    public final StringPath country = createString("COUNTRY");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> medicarenumber = createNumber("MEDICARENUMBER", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SPersonid> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPerson> _person_PIDIDFK = createInvForeignKey(id, "PID_ID");

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable), "null", "personid_");
    }

    public SPersonid(Path<? extends SPersonid> path) {
        super(path.getType(), path.getMetadata(), "null", "personid_");
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata, "null", "personid_");
    }

}

