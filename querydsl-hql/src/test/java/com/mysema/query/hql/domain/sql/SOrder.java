package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SOrder is a Querydsl query type for SOrder
 */
@Table("ORDER_")
public class SOrder extends BeanPath<SOrder> implements RelationalPath<SOrder> {

    private static final long serialVersionUID = -1645479003;

    public static final SOrder order = new SOrder("ORDER_");

    public final NumberPath<Integer> customerId = createNumber("CUSTOMER_ID", Integer.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Short> paid = createNumber("PAID", Short.class);

    private Expression[] _all;

    public final PrimaryKey<SOrder> sql100819184436320 = new PrimaryKey<SOrder>(this, id);

    public final ForeignKey<SCustomer> fk8b7256f15325d064 = new ForeignKey<SCustomer>(this, customerId, "ID");

    public final ForeignKey<SCustomer> _fk27fbe3fee803b049 = new ForeignKey<SCustomer>(this, id, "CURRENTORDER_ID");

    public SOrder(String variable) {
        super(SOrder.class, forVariable(variable));
    }

    public SOrder(BeanPath<? extends SOrder> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SOrder(PathMetadata<?> metadata) {
        super(SOrder.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{customerId, id, paid};
        }
        return _all;
    }

    public PrimaryKey<SOrder> getPrimaryKey() {
        return sql100819184436320;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk8b7256f15325d064);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk27fbe3fee803b049);
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

