/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;

import java.util.function.Function;

/**
 * {@code TypedQuery} is a typed query implementation for Lucene queries.
 *
 * <p>Converts Lucene documents to typed results via a constructor supplied transformer</p>
 *
 * @param <T> result type
 *
 * @author laim
 * @author tiwe
 */
public class TypedQuery<T> extends AbstractLuceneQuery<T, TypedQuery<T>> {

    /**
     * Create a new TypedQuery instance
     *
     * @param searcher index searcher
     * @param transformer transformer to transform Lucene documents to result objects
     */
    public TypedQuery(IndexSearcher searcher, Function<Document, T> transformer) {
        super(searcher, transformer);
    }

    /**
     * Create a new TypedQuery instance
     *
     * @param serializer serializer
     * @param searcher index search
     * @param transformer transformer to transform documents to result objects
     */
    public TypedQuery(LuceneSerializer serializer, IndexSearcher searcher, Function<Document, T> transformer) {
        super(serializer, searcher, transformer);
    }

}
