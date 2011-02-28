package com.mysema.query.mongodb;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.ArrayPath;

/**
 * @author tiwe
 *
 */
public class DoubleArray extends ArrayPath<Double>{
    
    private static final long serialVersionUID = 1776628530121566388L;

    public DoubleArray(String variable) {
        super(Double[].class, variable);
    }
    
    public DoubleArray(Path<?> parent, String property) {
        super(Double[].class, parent, property);
    }
    
    public DoubleArray(PathMetadata<?> metadata) {
        super(Double[].class, metadata);
    }

//    public BooleanExpression near(double latVal, double longVal){
//        return new PredicateOperation(this, new ConstantImpl(latVal), new ConstantImpl()
//    }
    
}
