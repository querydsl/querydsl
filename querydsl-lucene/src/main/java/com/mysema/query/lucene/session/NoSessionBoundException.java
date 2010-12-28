package com.mysema.query.lucene.session;

import com.mysema.query.QueryException;

public class NoSessionBoundException extends QueryException {

    private static final long serialVersionUID = 4569418223905066659L;

    public NoSessionBoundException(String msg) {
        super(msg);
    }

    public NoSessionBoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public NoSessionBoundException(Throwable t) {
        super(t);
    }

}
