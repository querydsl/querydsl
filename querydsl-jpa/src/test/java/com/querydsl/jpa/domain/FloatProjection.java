package com.querydsl.jpa.domain;

import com.querydsl.core.annotations.QueryProjection;

public class FloatProjection {
    
    public float val;
    
    @QueryProjection
    public FloatProjection(float val) {
        this.val = val;
    }

}
