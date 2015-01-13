package com.querydsl.jpa.domain;

import com.querydsl.core.annotations.QueryProjection;

public class DoubleProjection {
    
    public double val;
    
    @QueryProjection
    public DoubleProjection(double val) {
        this.val = val;
    }

}
