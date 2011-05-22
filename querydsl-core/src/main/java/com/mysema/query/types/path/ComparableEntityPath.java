package com.mysema.query.types.path;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class ComparableEntityPath<T extends Comparable> extends ComparablePath<T> implements EntityPath<T> {

    private static final long serialVersionUID = -7115848171352092315L;

    public ComparableEntityPath(Class<? extends T> type, Path<?> parent, String property) {
        super(type, parent, property);
    }

    public ComparableEntityPath(Class<? extends T> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    public ComparableEntityPath(Class<? extends T> type, String var) {
        super(type, var);
    }

}
