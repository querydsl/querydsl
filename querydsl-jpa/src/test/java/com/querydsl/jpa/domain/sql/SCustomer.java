package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCustomer is a Querydsl querydsl type for SCustomer
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCustomer extends com.querydsl.sql.RelationalPathBase<SCustomer> {

    private static final long serialVersionUID = -564764048;

    public static final SCustomer customer_ = new SCustomer("customer_");

    public final NumberPath<Long> currentOrderId = createNumber("currentOrderId", Long.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> nameId = createNumber("nameId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SCustomer> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SName> fk600e7c4196a83d9c = createForeignKey(nameId, "id");

    public final com.querydsl.sql.ForeignKey<SOrder> fk600e7c419cc457f1 = createForeignKey(currentOrderId, "id");

    public final com.querydsl.sql.ForeignKey<SStore_customer> _fk82ba2ce051f3c3e5 = createInvForeignKey(id, "customers_id");

    public final com.querydsl.sql.ForeignKey<SOrder> _fkc3df62d1b29c27bc = createInvForeignKey(id, "customer_id");

    public SCustomer(String variable) {
        super(SCustomer.class, forVariable(variable), "", "customer_");
        addMetadata();
    }

    public SCustomer(String variable, String schema, String table) {
        super(SCustomer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCustomer(Path<? extends SCustomer> path) {
        super(path.getType(), path.getMetadata(), "", "customer_");
        addMetadata();
    }

    public SCustomer(PathMetadata<?> metadata) {
        super(SCustomer.class, metadata, "", "customer_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(currentOrderId, ColumnMetadata.named("currentOrder_id").withIndex(2).ofType(-5).withSize(19));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(nameId, ColumnMetadata.named("name_id").withIndex(3).ofType(-5).withSize(19));
    }

}

