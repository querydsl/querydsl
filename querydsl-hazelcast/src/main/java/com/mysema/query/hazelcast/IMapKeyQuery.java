package com.mysema.query.hazelcast;

import java.util.Collection;

import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.mysema.query.SimpleQuery;

/**
 * IMapKeyQuery is the implementation of the {@link SimpleQuery} for Hazelcast {@link IMap#keySet(Predicate)}
 * 
 * @author velo
 */
public class IMapKeyQuery<Q> extends AbstractIMapQuery<Q>   {

    private IMap<Q, ?> map;

    public IMapKeyQuery(IMap<Q, ?> map) {
        this.map = map;
    }

    @Override
    protected Collection<Q> query(Predicate<?, Q> query) {
        return map.keySet(query);
    }

}
