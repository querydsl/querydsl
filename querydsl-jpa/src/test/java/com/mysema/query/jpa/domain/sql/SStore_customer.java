package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStore_customer is a Querydsl query type for SStore_customer
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStore_customer extends com.mysema.query.sql.RelationalPathBase<SStore_customer> {

    private static final long serialVersionUID = -649411411;

    public static final SStore_customer store_customer = new SStore_customer("store__customer_");

    public final NumberPath<Integer> customersID = createNumber("customers_ID", Integer.class);

    public final NumberPath<Long> store_id = createNumber("store__id", Long.class);

    public final NumberPath<Long> storeID = createNumber("Store_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SStore_customer> primary = createPrimaryKey(storeID, customersID);

    public final com.mysema.query.sql.ForeignKey<SStore> store_customer_StoreIDFK = createForeignKey(storeID, "ID");

    public final com.mysema.query.sql.ForeignKey<SStore> fk82ba2ce035d2d6bb = createForeignKey(store_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SCustomer> store_customer_customersIDFK = createForeignKey(customersID, "id");

    public SStore_customer(String variable) {
        super(SStore_customer.class, forVariable(variable), "null", "store__customer_");
    }

    public SStore_customer(Path<? extends SStore_customer> path) {
        super(path.getType(), path.getMetadata(), "null", "store__customer_");
    }

    public SStore_customer(PathMetadata<?> metadata) {
        super(SStore_customer.class, metadata, "null", "store__customer_");
    }

}

