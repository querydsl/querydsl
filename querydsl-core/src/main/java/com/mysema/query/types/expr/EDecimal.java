/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops.MathOps;

/**
 * EDecimal represents decimal expressions
 * 
 * @author tiwe
 *
 * @param <D>
 */
public abstract class EDecimal<D extends Number & Comparable<?>> extends ENumber<D> {

    public EDecimal(Class<? extends D> type) {
        super(type);
    }
    
    private ENumber<D> round, floor, ceil;
    
    /**
     * @return round(this)
     * @see java.lang.Math#round(double)
     * @see java.lang.Math#round(float)
     */
    public ENumber<D> round() {
        if (round == null){
            round = ONumber.create(getType(), MathOps.ROUND, this); 
        }
        return round;
    }
    
    /**
     * @return floor(this)
     * @see java.lang.Math#floor(double)
     */
    public ENumber<D> floor() {
        if (floor == null){
            floor = ONumber.create(getType(), MathOps.FLOOR, this);
        }
        return floor;
    }
    
    /**
     * @return ceil(this)
     * @see java.lang.Math#ceil(double)
     */
    public ENumber<D> ceil() {
        if (ceil == null){
            ceil = ONumber.create(getType(), MathOps.CEIL, this); 
        }
        return ceil;
    }

}
