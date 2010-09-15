/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import javax.annotation.Nonnegative;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
public final class PathMetadataFactory {

    public static PathMetadata<Integer> forArrayAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.ARRAYVALUE);
    }

    public static PathMetadata<Integer> forArrayAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, new ConstantImpl<Integer>(index), PathType.ARRAYVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forListAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, new ConstantImpl<Integer>(index), PathType.LISTVALUE_CONSTANT);
    }

    public static <KT> PathMetadata<KT> forMapAccess(Path<?> parent, Expression<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(Path<?> parent, KT key) {
        return new PathMetadata<KT>(parent, new ConstantImpl<KT>(key), PathType.MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent, String property) {
        return new PathMetadata<String>(parent, new ConstantImpl<String>(Assert.hasLength(property,"property")), PathType.PROPERTY);
    }

    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, new ConstantImpl<String>(Assert.hasLength(variable,"variable")), PathType.VARIABLE);
    }

    private PathMetadataFactory(){}

}
