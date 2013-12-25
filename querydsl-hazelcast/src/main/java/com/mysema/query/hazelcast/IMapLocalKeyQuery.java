package com.mysema.query.hazelcast;

import java.util.Collection;

import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.mysema.query.SimpleQuery;

/**
 * IMapLocalKeyQuery is the implementation of the {@link SimpleQuery} for Hazelcast {@link IMap#localKeySet(Predicate)}
 * 
 * @author velo
 */
public class IMapLocalKeyQuery<Q> extends AbstractIMapQuery<Q> {

    private IMap<Q, ?> map;

    public IMapLocalKeyQuery(IMap<Q, ?> map) {
        super();
        this.map = map;
    }

    @Override
    protected Collection<Q> query(Predicate<?, Q> query) {
        return map.localKeySet(query);
    }

}
