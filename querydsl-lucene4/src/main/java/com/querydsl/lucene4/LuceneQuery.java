/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.lucene4;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;

import com.google.common.base.Function;

/**
 * LuceneQuery is a Querydsl querydsl implementation for Lucene queries.
 *
 * @author vema
 */
public class LuceneQuery extends AbstractLuceneQuery<Document, LuceneQuery> {
    
    private static final Function<Document,Document> TRANSFORMER = new Function<Document,Document>() {
        @Override
        public Document apply(Document input) {
            return input;
        }        
    };
  
    public LuceneQuery(IndexSearcher searcher) {
        super(searcher, TRANSFORMER);
    }

    public LuceneQuery(LuceneSerializer luceneSerializer, IndexSearcher searcher) {
        super(luceneSerializer, searcher, TRANSFORMER);
    }

}
