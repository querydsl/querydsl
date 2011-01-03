package com.mysema.query.lucene.session;

import com.mysema.query.QueryException;

public class WriteLockObtainFailedException extends QueryException {

    private static final long serialVersionUID = 4569418223905066659L;

    public WriteLockObtainFailedException(String msg) {
        super(msg);
    }

    public WriteLockObtainFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public WriteLockObtainFailedException(Throwable t) {
        super(t);
    }

}
