/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import javax.annotation.Nonnegative;

import com.mysema.commons.lang.Assert;

/**
 * PathMetadataFactory is a factory class for Path construction
 * 
 * @author tiwe
 *
 */
public final class PathMetadataFactory {

    public static PathMetadata<Integer> forArrayAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.ARRAYVALUE);
    }

    public static PathMetadata<Integer> forArrayAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, ConstantImpl.create(index), PathType.ARRAYVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forListAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, ConstantImpl.create(index), PathType.LISTVALUE_CONSTANT);
    }

    public static <KT> PathMetadata<KT> forMapAccess(Path<?> parent, Expression<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(Path<?> parent, KT key) {
        return new PathMetadata<KT>(parent, new ConstantImpl<KT>(key), PathType.MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent, String property) {
        return new PathMetadata<String>(parent, ConstantImpl.create(Assert.hasLength(property,"property"), true), PathType.PROPERTY);
    }

    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, ConstantImpl.create(Assert.hasLength(variable,"variable"), true), PathType.VARIABLE);
    }

    private PathMetadataFactory(){}

}
