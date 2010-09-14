/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import javax.annotation.Nonnegative;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathType;
import com.mysema.query.types.expr.NumberConstant;
import com.mysema.query.types.expr.StringConstant;
import com.mysema.query.types.expr.SimpleConstant;

/**
 * @author tiwe
 *
 */
public final class PathMetadataFactory {

    public static PathMetadata<Integer> forArrayAccess(ArrayPath<?> parent, Expression<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.ARRAYVALUE);
    }

    public static PathMetadata<Integer> forArrayAccess(ArrayPath<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, NumberConstant.create(index), PathType.ARRAYVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forListAccess(ListPath<?, ?> parent, Expression<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(ListPath<?, ?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, NumberConstant.create(index), PathType.LISTVALUE_CONSTANT);
    }

    public static <KT> PathMetadata<KT> forMapAccess(MapPath<?, ?, ?> parent, Expression<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(MapPath<?, ?, ?> parent, KT key) {
        return new PathMetadata<KT>(parent, SimpleConstant.create(key), PathType.MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent, String property) {
        return new PathMetadata<String>(parent, StringConstant.create(Assert.hasLength(property,"property"), true), PathType.PROPERTY);
    }

    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, StringConstant.create(Assert.hasLength(variable,"variable"), true), PathType.VARIABLE);
    }

    private PathMetadataFactory(){}

}
