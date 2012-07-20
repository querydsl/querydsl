package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCustomer is a Querydsl query type for SCustomer
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCustomer extends com.mysema.query.sql.RelationalPathBase<SCustomer> {

    private static final long serialVersionUID = 1228707791;

    public static final SCustomer customer = new SCustomer("customer_");

    public final NumberPath<Long> currentOrderId = createNumber("currentOrder_id", Long.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> nameId = createNumber("name_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SCustomer> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SOrder> customer_CURRENTORDERIDFK = createForeignKey(currentOrderId, "id");

    public final com.mysema.query.sql.ForeignKey<SName> fk600e7c4196a83d9c = createForeignKey(nameId, "id");

    public final com.mysema.query.sql.ForeignKey<SName> customer_NAMEIDFK = createForeignKey(nameId, "id");

    public final com.mysema.query.sql.ForeignKey<SOrder> fk600e7c419cc457f1 = createForeignKey(currentOrderId, "id");

    public final com.mysema.query.sql.ForeignKey<SOrder> _fkc3df62d1b29c27bc = createInvForeignKey(id, "customer_id");

    public final com.mysema.query.sql.ForeignKey<SStore_customer> _store_customer_customersIDFK = createInvForeignKey(id, "customers_ID");

    public final com.mysema.query.sql.ForeignKey<SOrder> _order_CUSTOMERIDFK = createInvForeignKey(id, "customer_id");

    public SCustomer(String variable) {
        super(SCustomer.class, forVariable(variable), "null", "customer_");
    }

    public SCustomer(Path<? extends SCustomer> path) {
        super(path.getType(), path.getMetadata(), "null", "customer_");
    }

    public SCustomer(PathMetadata<?> metadata) {
        super(SCustomer.class, metadata, "null", "customer_");
    }

}

