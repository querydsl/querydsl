package com.mysema.query.sql;

import java.util.Map;

import com.mysema.query.sql.RelationalPath;

/**
 * Contains a list of beans
 * @author luis
 */
public class Beans {

    private final Map<RelationalPath<?>, ?> beans;

    public Beans(Map<RelationalPath<?>, ?> beans) {
        this.beans = beans;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(RelationalPath<T> path) {
        return (T) beans.get(path);
    }

}