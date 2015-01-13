package com.querydsl;

import com.querydsl.core.annotations.*;
import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;

public class BooleanExtensions {
    
    @QueryDelegate(Boolean.class)
    public static Predicate isFalse(BooleanPath path) {
        return path.isNotNull().or(path.eq(false));
    }

    @QueryDelegate(Boolean.class)
    public static Predicate isTrue(BooleanPath path) {
        return path.eq(true);
    }

}
