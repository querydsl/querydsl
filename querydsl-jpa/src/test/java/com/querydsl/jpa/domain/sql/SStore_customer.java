package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SStore_customer is a Querydsl querydsl type for SStore_customer
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SStore_customer extends com.querydsl.sql.RelationalPathBase<SStore_customer> {

    private static final long serialVersionUID = 1343082834;

    public static final SStore_customer store_customer_ = new SStore_customer("store__customer_");

    public final NumberPath<Integer> customersId = createNumber("customersId", Integer.class);

    public final NumberPath<Long> store_id = createNumber("store_id", Long.class);

    public final com.querydsl.sql.ForeignKey<SStore> fk82ba2ce035d2d6bb = createForeignKey(store_id, "id");

    public final com.querydsl.sql.ForeignKey<SCustomer> fk82ba2ce051f3c3e5 = createForeignKey(customersId, "id");

    public SStore_customer(String variable) {
        super(SStore_customer.class, forVariable(variable), "", "store__customer_");
        addMetadata();
    }

    public SStore_customer(String variable, String schema, String table) {
        super(SStore_customer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SStore_customer(Path<? extends SStore_customer> path) {
        super(path.getType(), path.getMetadata(), "", "store__customer_");
        addMetadata();
    }

    public SStore_customer(PathMetadata<?> metadata) {
        super(SStore_customer.class, metadata, "", "store__customer_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(customersId, ColumnMetadata.named("customers_id").withIndex(2).ofType(4).withSize(10).notNull());
        addMetadata(store_id, ColumnMetadata.named("store__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

