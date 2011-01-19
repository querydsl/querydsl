package com.mysema.query.sql;

import java.util.Map;

import com.mysema.query.sql.RelationalPath;

/**
 * Beans contains a list of beans
 * 
 * @author luis
 */
public class Beans {

    private final Map<? extends RelationalPath<?>, ?> beans;

    public Beans(Map<? extends RelationalPath<?>, ?> beans) {
        this.beans = beans;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(RelationalPath<T> path) {
        return (T) beans.get(path);
    }

}