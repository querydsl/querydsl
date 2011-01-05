package com.mysema.query.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;

import com.mysema.commons.lang.CloseableIterator;

public interface ResultTransformer<T> {

    CloseableIterator<T> getIterator(ScoreDoc[] scoreDocs, int offset, Searcher searcher);

    T transform(Document doc);

}
