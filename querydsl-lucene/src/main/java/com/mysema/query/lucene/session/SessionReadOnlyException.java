package com.mysema.query.lucene.session;

import com.mysema.query.QueryException;

public class SessionReadOnlyException extends QueryException {

    private static final long serialVersionUID = -4561275733479044147L;

    public SessionReadOnlyException(String msg) {
        super(msg);
    }

    public SessionReadOnlyException(String msg, Throwable t) {
        super(msg, t);
    }

    public SessionReadOnlyException(Throwable t) {
        super(t);
    }

}
