package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SStore_customer is a Querydsl query type for SStore_customer
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStore_customer extends com.mysema.query.sql.RelationalPathBase<SStore_customer> {

    private static final long serialVersionUID = 1343082834;

    public static final SStore_customer store_customer_ = new SStore_customer("store__customer_");

    public final NumberPath<Integer> customersId = createNumber("customersId", Integer.class);

    public final NumberPath<Long> store_id = createNumber("store_id", Long.class);

    public final com.mysema.query.sql.ForeignKey<SStore> fk82ba2ce035d2d6bb = createForeignKey(store_id, "id");

    public final com.mysema.query.sql.ForeignKey<SCustomer> fk82ba2ce051f3c3e5 = createForeignKey(customersId, "id");

    public SStore_customer(String variable) {
        super(SStore_customer.class, forVariable(variable), "null", "store__customer_");
        addMetadata();
    }

    public SStore_customer(String variable, String schema, String table) {
        super(SStore_customer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SStore_customer(Path<? extends SStore_customer> path) {
        super(path.getType(), path.getMetadata(), "null", "store__customer_");
        addMetadata();
    }

    public SStore_customer(PathMetadata<?> metadata) {
        super(SStore_customer.class, metadata, "null", "store__customer_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(customersId, ColumnMetadata.named("customers_id").withIndex(2).ofType(4).withSize(10).notNull());
        addMetadata(store_id, ColumnMetadata.named("store__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

