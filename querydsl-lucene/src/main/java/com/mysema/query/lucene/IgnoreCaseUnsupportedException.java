package com.mysema.query.lucene;

/**
 * @author tiwe
 *
 */
public class IgnoreCaseUnsupportedException extends UnsupportedOperationException{

    private static final long serialVersionUID = 412913389929530788L;

    public IgnoreCaseUnsupportedException() {
        super("Ignore case queries are not supported with Lucene");
    }
    
}
