package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStore_customer is a Querydsl query type for SStore_customer
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SStore_customer extends com.mysema.query.sql.RelationalPathBase<SStore_customer> {

    private static final long serialVersionUID = -649411411;

    public static final SStore_customer store_customer = new SStore_customer("STORE__CUSTOMER_");

    public final NumberPath<Integer> customersId = createNumber("CUSTOMERS_ID", Integer.class);

    public final NumberPath<Long> store_id = createNumber("STORE__ID", Long.class);

    public final com.mysema.query.sql.ForeignKey<SStore> fk82ba2ce035d2d6bb = createForeignKey(store_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SCustomer> fk82ba2ce051f3c3e5 = createForeignKey(customersId, "ID");

    public SStore_customer(String variable) {
        super(SStore_customer.class, forVariable(variable), "APP", "STORE__CUSTOMER_");
    }

    public SStore_customer(Path<? extends SStore_customer> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "STORE__CUSTOMER_");
    }

    public SStore_customer(PathMetadata<?> metadata) {
        super(SStore_customer.class, metadata, "APP", "STORE__CUSTOMER_");
    }

}

