package com.mysema.query.jdoql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class JDOTuple implements Tuple{

    private final List<Object> indexed = new ArrayList<Object>();

    private final Map<String,Object> mapped = new LinkedHashMap<String,Object>();

    public void put(Object name, Object value){
    indexed.add(value);
    mapped.put(name.toString(), value);
    }

    @Override
    public <T> T get(int index, Class<T> type) {
    return (T) indexed.get(index);
    }

    @Override
    public <T> T get(Expr<T> expr) {
    if (expr instanceof Path){
        return (T) mapped.get(((Path)expr).getMetadata().getExpression().toString());
    }else{
        return (T) mapped.get(expr.toString());
    }
    }

    @Override
    public Object[] toArray() {
    return indexed.toArray();
    }

}
