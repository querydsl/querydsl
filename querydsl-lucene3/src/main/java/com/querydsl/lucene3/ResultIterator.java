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
package com.querydsl.lucene3;

import java.io.IOException;

import javax.annotation.Nullable;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

import com.google.common.base.Function;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryException;

/**
 * ResultIterator is a {@link CloseableIterator} implementation for Lucene querydsl results
 * 
 * @author tiwe
 *
 * @param <T>
 */
public final class ResultIterator<T> implements CloseableIterator<T> {

    private final ScoreDoc[] scoreDocs;

    private int cursor;

    private final IndexSearcher searcher;

    @Nullable
    private final FieldSelector fieldSelector;

    private final Function<Document,T> transformer;

    public ResultIterator(ScoreDoc[] scoreDocs, int offset, IndexSearcher searcher, 
            @Nullable FieldSelector fieldSelector, Function<Document, T> transformer) {
        this.scoreDocs = scoreDocs.clone();
        this.cursor = offset;
        this.searcher = searcher;
        this.fieldSelector = fieldSelector;
        this.transformer = transformer;
    }

    @Override
    public boolean hasNext() {
        return cursor != scoreDocs.length;
    }

    @Override
    public T next() {
        try {
            Document document;
            if (fieldSelector != null) {
                document = searcher.doc(scoreDocs[cursor++].doc, fieldSelector);
            } else {
                document = searcher.doc(scoreDocs[cursor++].doc);
            }
            return transformer.apply(document);
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {

    }

}