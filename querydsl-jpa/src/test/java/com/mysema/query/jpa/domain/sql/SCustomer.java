package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCustomer is a Querydsl query type for SCustomer
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCustomer extends com.mysema.query.sql.RelationalPathBase<SCustomer> {

    private static final long serialVersionUID = 1228707791;

    public static final SCustomer customer = new SCustomer("CUSTOMER_");

    public final NumberPath<Long> currentorderId = createNumber("CURRENTORDER_ID", Long.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> nameId = createNumber("NAME_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCustomer> sql120219232322890 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SName> fk600e7c4196a83d9c = createForeignKey(nameId, "ID");

    public final com.mysema.query.sql.ForeignKey<SOrder> fk600e7c419cc457f1 = createForeignKey(currentorderId, "ID");

    public final com.mysema.query.sql.ForeignKey<SStore_customer> _fk82ba2ce051f3c3e5 = createInvForeignKey(id, "CUSTOMERS_ID");

    public final com.mysema.query.sql.ForeignKey<SOrder> _fkc3df62d1b29c27bc = createInvForeignKey(id, "CUSTOMER_ID");

    public SCustomer(String variable) {
        super(SCustomer.class, forVariable(variable), "APP", "CUSTOMER_");
    }

    public SCustomer(Path<? extends SCustomer> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "CUSTOMER_");
    }

    public SCustomer(PathMetadata<?> metadata) {
        super(SCustomer.class, metadata, "APP", "CUSTOMER_");
    }

}

