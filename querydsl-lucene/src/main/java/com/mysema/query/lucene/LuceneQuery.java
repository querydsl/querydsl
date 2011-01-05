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
 * LuceneQuery is a Querydsl query implementation for Lucene queries.
 *
 * @author vema
 */
public class LuceneQuery extends AbstractLuceneQuery<Document, LuceneQuery>{
    
    private static final Transformer<Document,Document> TRANSFORMER = new Transformer<Document,Document>() {

        @Override
        public Document transform(Document input) {
            return input;
        }
        
    };
  
    public LuceneQuery(Searcher searcher) {
        super(searcher, TRANSFORMER);
    }

    public LuceneQuery(LuceneSerializer luceneSerializer, Searcher searcher) {
        super(luceneSerializer, searcher, TRANSFORMER);
    }

}
