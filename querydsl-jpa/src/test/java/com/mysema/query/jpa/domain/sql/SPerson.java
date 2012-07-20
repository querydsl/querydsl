package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPerson is a Querydsl query type for SPerson
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPerson extends com.mysema.query.sql.RelationalPathBase<SPerson> {

    private static final long serialVersionUID = 756415366;

    public static final SPerson person = new SPerson("person_");

    public final DatePath<java.sql.Date> birthday = createDate("BIRTHDAY", java.sql.Date.class);

    public final NumberPath<Long> i = createNumber("I", Long.class);

    public final StringPath name = createString("NAME");

    public final NumberPath<Long> nationalityId = createNumber("NATIONALITY_ID", Long.class);

    public final NumberPath<Long> pidId = createNumber("PID_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SPerson> primary = createPrimaryKey(i);

    public final com.mysema.query.sql.ForeignKey<SPersonid> person_PIDIDFK = createForeignKey(pidId, "ID");

    public final com.mysema.query.sql.ForeignKey<SNationality> person_NATIONALITYIDFK = createForeignKey(nationalityId, "ID");

    public final com.mysema.query.sql.ForeignKey<SAccount> _account_OWNERIFK = createInvForeignKey(i, "OWNER_I");

    public SPerson(String variable) {
        super(SPerson.class, forVariable(variable), "null", "person_");
    }

    public SPerson(Path<? extends SPerson> path) {
        super(path.getType(), path.getMetadata(), "null", "person_");
    }

    public SPerson(PathMetadata<?> metadata) {
        super(SPerson.class, metadata, "null", "person_");
    }

}

