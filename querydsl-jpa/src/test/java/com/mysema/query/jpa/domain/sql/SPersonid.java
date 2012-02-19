package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPersonid is a Querydsl query type for SPersonid
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SPersonid extends com.mysema.query.sql.RelationalPathBase<SPersonid> {

    private static final long serialVersionUID = 1065697057;

    public static final SPersonid personid = new SPersonid("PERSONID_");

    public final StringPath country = createString("COUNTRY");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> medicarenumber = createNumber("MEDICARENUMBER", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SPersonid> sql120219232328340 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPerson> _fkd78fcfaad7999e61 = createInvForeignKey(id, "PID_ID");

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable), "APP", "PERSONID_");
    }

    public SPersonid(Path<? extends SPersonid> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "PERSONID_");
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata, "APP", "PERSONID_");
    }

}

