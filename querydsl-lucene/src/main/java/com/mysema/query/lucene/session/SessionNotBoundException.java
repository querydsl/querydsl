package com.mysema.query.lucene.session;

import com.mysema.query.QueryException;

public class SessionNotBoundException extends QueryException {

    private static final long serialVersionUID = 4569418223905066659L;

    public SessionNotBoundException(String msg) {
        super(msg);
    }

    public SessionNotBoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public SessionNotBoundException(Throwable t) {
        super(t);
    }

}
