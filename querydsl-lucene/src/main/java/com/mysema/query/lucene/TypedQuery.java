/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.lucene;

import org.apache.commons.collections15.Transformer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Searcher;

/**
 * LuceneQuery is a typed query implementation for Lucene queries.
 * 
 * @author vema
 */
public class TypedQuery<T> extends AbstractLuceneQuery<T, TypedQuery<T>> {

    public TypedQuery(Searcher searcher, Transformer<Document, T> transformer) {
        super(searcher, transformer);
    }

    public TypedQuery(LuceneSerializer serializer, Searcher searcher, Transformer<Document, T> transformer) {
        super(serializer, searcher, transformer);
    }
  
}
