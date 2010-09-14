package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SAccount is a Querydsl query type for SAccount
 */
@Table("ACCOUNT")
public class SAccount extends RelationalPathBase<SAccount> {

    private static final long serialVersionUID = -727563068;

    public static final SAccount account = new SAccount("ACCOUNT");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> ownerI = createNumber("OWNER_I", Long.class);

    public final StringPath somedata = createString("SOMEDATA");

    public final PrimaryKey<SAccount> sql100819184429820 = createPrimaryKey(id);

    public final ForeignKey<SPerson> fk1d0c220d257b5f1c = new ForeignKey<SPerson>(this, ownerI, "I");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable));
    }

    public SAccount(BeanPath<? extends SAccount> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata);
    }

}

