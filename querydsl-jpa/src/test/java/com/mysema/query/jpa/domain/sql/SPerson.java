package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPerson is a Querydsl query type for SPerson
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SPerson extends com.mysema.query.sql.RelationalPathBase<SPerson> {

    private static final long serialVersionUID = 756415366;

    public static final SPerson person = new SPerson("PERSON_");

    public final DatePath<java.sql.Date> birthday = createDate("BIRTHDAY", java.sql.Date.class);

    public final NumberPath<Long> i = createNumber("I", Long.class);

    public final StringPath name = createString("NAME");

    public final NumberPath<Long> nationalityId = createNumber("NATIONALITY_ID", Long.class);

    public final NumberPath<Long> pidId = createNumber("PID_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SPerson> sql120219232328120 = createPrimaryKey(i);

    public final com.mysema.query.sql.ForeignKey<SPersonid> fkd78fcfaad7999e61 = createForeignKey(pidId, "ID");

    public final com.mysema.query.sql.ForeignKey<SNationality> fkd78fcfaaf6578e38 = createForeignKey(nationalityId, "ID");

    public final com.mysema.query.sql.ForeignKey<SAccount> _fk809dbbd28cfac74 = createInvForeignKey(i, "OWNER_I");

    public SPerson(String variable) {
        super(SPerson.class, forVariable(variable), "APP", "PERSON_");
    }

    public SPerson(Path<? extends SPerson> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "PERSON_");
    }

    public SPerson(PathMetadata<?> metadata) {
        super(SPerson.class, metadata, "APP", "PERSON_");
    }

}

