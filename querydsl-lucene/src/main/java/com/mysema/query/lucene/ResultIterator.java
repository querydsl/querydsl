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
package com.mysema.query.lucene;

import java.io.IOException;

import javax.annotation.Nullable;

import org.apache.commons.collections15.Transformer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.QueryException;

/**
 * @author tiwe
 *
 * @param <T>
 */
public final class ResultIterator<T> implements CloseableIterator<T> {

    private final ScoreDoc[] scoreDocs;

    private int cursor;

    private final Searcher searcher;

    @Nullable
    private final FieldSelector fieldSelector;

    private final Transformer<Document,T> transformer;

    public ResultIterator(ScoreDoc[] scoreDocs, int offset, Searcher searcher, @Nullable FieldSelector fieldSelector, Transformer<Document, T> transformer) {
        this.scoreDocs = scoreDocs;
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
            return transformer.transform(document);
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