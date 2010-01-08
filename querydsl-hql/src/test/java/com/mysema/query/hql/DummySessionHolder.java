package com.mysema.query.hql;

import org.hibernate.Query;

import com.mysema.query.hql.hibernate.SessionHolder;

public class DummySessionHolder implements SessionHolder{

    @Override
    public Query createQuery(String queryString) {
        return null;
    }

}
