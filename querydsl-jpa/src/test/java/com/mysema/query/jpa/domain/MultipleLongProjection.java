package com.mysema.query.jpa.domain;

import com.mysema.query.annotations.QueryProjection;

public class MultipleLongProjection {
    
    public float val1;
    public float val2;
    
    @QueryProjection
    public MultipleLongProjection(long val1, long val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

}
