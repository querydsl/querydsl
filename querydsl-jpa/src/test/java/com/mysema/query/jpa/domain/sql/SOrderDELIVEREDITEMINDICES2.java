package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SOrderDELIVEREDITEMINDICES is a Querydsl query type for SOrderDELIVEREDITEMINDICES
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SOrderDELIVEREDITEMINDICES2 extends com.mysema.query.sql.RelationalPathBase<SOrderDELIVEREDITEMINDICES2> {

    private static final long serialVersionUID = 541359789;

    public static final SOrderDELIVEREDITEMINDICES2 OrderDELIVEREDITEMINDICES = new SOrderDELIVEREDITEMINDICES2("Order_DELIVEREDITEMINDICES");

    public final NumberPath<Integer> delivereditemindices = createNumber("DELIVEREDITEMINDICES", Integer.class);

    public final NumberPath<Long> orderID = createNumber("Order_ID", Long.class);

    public final com.mysema.query.sql.ForeignKey<SOrder> orderDELIVEREDITEMINDICESOrderIDFK = createForeignKey(orderID, "id");

    public SOrderDELIVEREDITEMINDICES2(String variable) {
        super(SOrderDELIVEREDITEMINDICES2.class, forVariable(variable), "null", "Order_DELIVEREDITEMINDICES");
    }

    public SOrderDELIVEREDITEMINDICES2(Path<? extends SOrderDELIVEREDITEMINDICES2> path) {
        super(path.getType(), path.getMetadata(), "null", "Order_DELIVEREDITEMINDICES");
    }

    public SOrderDELIVEREDITEMINDICES2(PathMetadata<?> metadata) {
        super(SOrderDELIVEREDITEMINDICES2.class, metadata, "null", "Order_DELIVEREDITEMINDICES");
    }

}

