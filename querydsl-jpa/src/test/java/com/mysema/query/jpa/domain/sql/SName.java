package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SName is a Querydsl query type for SName
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SName extends com.mysema.query.sql.RelationalPathBase<SName> {

    private static final long serialVersionUID = -1389219076;

    public static final SName name = new SName("name_");

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final StringPath nickName = createString("nickName");

    public final com.mysema.query.sql.PrimaryKey<SName> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCustomer> _fk600e7c4196a83d9c = createInvForeignKey(id, "name_id");

    public final com.mysema.query.sql.ForeignKey<SCustomer> _customer_NAMEIDFK = createInvForeignKey(id, "name_id");

    public SName(String variable) {
        super(SName.class, forVariable(variable), "null", "name_");
    }

    public SName(Path<? extends SName> path) {
        super(path.getType(), path.getMetadata(), "null", "name_");
    }

    public SName(PathMetadata<?> metadata) {
        super(SName.class, metadata, "null", "name_");
    }

}

