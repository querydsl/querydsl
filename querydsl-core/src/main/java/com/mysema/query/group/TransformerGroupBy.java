package com.mysema.query.group;

import java.util.Map;

import org.apache.commons.collections15.Transformer;

import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;

public class TransformerGroupBy<K, V> implements ResultTransformer<Map<K, V>> {

    private final GroupBy<K> groupBy; 
    
    private final Transformer<? super Group, ? extends V> transformer;
    
    public static <K, V> TransformerGroupBy<K, V> create(GroupBy<K> groupBy, Transformer<? super Group, ? extends V> transformer) {
        return new TransformerGroupBy<K, V>(groupBy, transformer);
    }
    
    public TransformerGroupBy(GroupBy<K> groupBy, Transformer<? super Group, ? extends V> transformer) {
        this.groupBy = groupBy;
        this.transformer = transformer;
    }

    @Override
    public Map<K, V> transform(Projectable projectable) {
        return TransformerMap.create(groupBy.transform(projectable), transformer);
    }
    
}
