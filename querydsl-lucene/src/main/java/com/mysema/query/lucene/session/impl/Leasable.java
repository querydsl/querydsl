package com.mysema.query.lucene.session.impl;

public interface Leasable {

    void lease();

    void release();

}
