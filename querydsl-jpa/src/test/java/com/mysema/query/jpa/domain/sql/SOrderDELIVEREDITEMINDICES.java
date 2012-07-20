package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SOrderDELIVEREDITEMINDICES is a Querydsl query type for SOrderDELIVEREDITEMINDICES
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SOrderDELIVEREDITEMINDICES extends com.mysema.query.sql.RelationalPathBase<SOrderDELIVEREDITEMINDICES> {

    private static final long serialVersionUID = 541359789;

    public static final SOrderDELIVEREDITEMINDICES OrderDELIVEREDITEMINDICES = new SOrderDELIVEREDITEMINDICES("Order_DELIVEREDITEMINDICES");

    public final NumberPath<Integer> delivereditemindices = createNumber("DELIVEREDITEMINDICES", Integer.class);

    public final NumberPath<Long> orderID = createNumber("Order_ID", Long.class);

    public final com.mysema.query.sql.ForeignKey<SOrder> orderDELIVEREDITEMINDICESOrderIDFK = createForeignKey(orderID, "id");

    public SOrderDELIVEREDITEMINDICES(String variable) {
        super(SOrderDELIVEREDITEMINDICES.class, forVariable(variable), "null", "Order_DELIVEREDITEMINDICES");
    }

    public SOrderDELIVEREDITEMINDICES(Path<? extends SOrderDELIVEREDITEMINDICES> path) {
        super(path.getType(), path.getMetadata(), "null", "Order_DELIVEREDITEMINDICES");
    }

    public SOrderDELIVEREDITEMINDICES(PathMetadata<?> metadata) {
        super(SOrderDELIVEREDITEMINDICES.class, metadata, "null", "Order_DELIVEREDITEMINDICES");
    }

}

