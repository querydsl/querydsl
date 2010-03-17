/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import javax.annotation.Nonnegative;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.expr.ExprConst;

/**
 * @author tiwe
 *
 */
public final class PathMetadataFactory {
    
    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.ARRAYVALUE);
    }
    
    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, ENumberConst.create(index), PathType.ARRAYVALUE_CONSTANT);
    }

    public static <T> PathMetadata<T> forDelegate(Expr<T> target){
        return new PathMetadata<T>(null, target, PathType.DELEGATE);
    }

    public static PathMetadata<Integer> forListAccess(PList<?, ?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(PList<?, ?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, ENumberConst.create(index), PathType.LISTVALUE_CONSTANT);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?, ?> parent, Expr<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?, ?> parent, KT key) {
        return new PathMetadata<KT>(parent, ExprConst.create(key), PathType.MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent, String property) {
        return new PathMetadata<String>(parent, EStringConst.create(Assert.hasLength(property), true), PathType.PROPERTY);
    }

    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, EStringConst.create(Assert.hasLength(variable), true), PathType.VARIABLE);
    }
    
    private PathMetadataFactory(){}

}
