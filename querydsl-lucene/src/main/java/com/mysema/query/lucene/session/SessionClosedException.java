package com.mysema.query.lucene.session;

import com.mysema.query.QueryException;

public class SessionClosedException extends QueryException {

    private static final long serialVersionUID = 4569418223905066659L;

    public SessionClosedException(String msg) {
        super(msg);
    }

    public SessionClosedException(String msg, Throwable t) {
        super(msg, t);
    }

    public SessionClosedException(Throwable t) {
        super(t);
    }

}
