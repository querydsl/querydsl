package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SName is a Querydsl query type for SName
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SName extends com.mysema.query.sql.RelationalPathBase<SName> {

    private static final long serialVersionUID = -1389219076;

    public static final SName name = new SName("NAME_");

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath lastname = createString("LASTNAME");

    public final StringPath nickname = createString("NICKNAME");

    public final com.mysema.query.sql.PrimaryKey<SName> sql120219232326390 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCustomer> _fk600e7c4196a83d9c = createInvForeignKey(id, "NAME_ID");

    public SName(String variable) {
        super(SName.class, forVariable(variable), "APP", "NAME_");
    }

    public SName(Path<? extends SName> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "NAME_");
    }

    public SName(PathMetadata<?> metadata) {
        super(SName.class, metadata, "APP", "NAME_");
    }

}

