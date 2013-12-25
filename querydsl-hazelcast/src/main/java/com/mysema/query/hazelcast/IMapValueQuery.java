package com.mysema.query.hazelcast;

import java.util.Collection;

import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.mysema.query.SimpleQuery;

/**
 * IMapValueQuery is the implementation of the {@link SimpleQuery} for Hazelcast {@link IMap#values(Predicate)}
 * 
 * @see IMap
 * @author velo
 */
public class IMapValueQuery<Q> extends AbstractIMapQuery<Q> {

    private IMap<?, Q> map;

    public IMapValueQuery(IMap<?, Q> map) {
        this.map = map;
    }

    @Override
    protected Collection<Q> query(Predicate<?, Q> query) {
        return map.values(query);
    }

}
