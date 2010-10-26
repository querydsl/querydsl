package com.mysema.query;

import com.mysema.query.annotations.*;
import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

public class BooleanExtensions2 {
    
    @QueryDelegate(boolean.class)
    public static Predicate isFalse(BooleanPath path) {
        return path.isNotNull().or(path.eq(false));
    }

    @QueryDelegate(boolean.class)
    public static Predicate isTrue(BooleanPath path) {
        return path.eq(true);
    }

}
