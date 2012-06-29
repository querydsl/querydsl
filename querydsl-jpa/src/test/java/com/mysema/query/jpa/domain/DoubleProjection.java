package com.mysema.query.jpa.domain;

import com.mysema.query.annotations.QueryProjection;

public class DoubleProjection {
    
    public double val;
    
    @QueryProjection
    public DoubleProjection(double val) {
        this.val = val;
    }

}
