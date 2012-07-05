package com.mysema.query.jpa.domain;

import com.mysema.query.annotations.QueryProjection;

public class FloatProjection {
    
    public float val;
    
    @QueryProjection
    public FloatProjection(float val) {
        this.val = val;
    }

}
