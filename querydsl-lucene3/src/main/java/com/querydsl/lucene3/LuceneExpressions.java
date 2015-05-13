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

import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * Utility methods to create filter expressions for Lucene queries that are not covered by the
 * Querydsl standard expression model
 *
 * @author tiwe
 *
 */
public final class LuceneExpressions {

    /**
     * Create a fuzzy query
     *
     * @param path path
     * @param value value to match
     * @return condition
     */
    public static BooleanExpression fuzzyLike(Path<String> path, String value) {
        Term term = new Term(path.getMetadata().getName(), value);
        return new QueryElement(new FuzzyQuery(term));
    }

    /**
     * Create a fuzzy query
     *
     * @param path path
     * @param value value to match
     * @param minimumSimilarity a value between 0 and 1 to set the required similarity
     * @return condition
     */
    public static BooleanExpression fuzzyLike(Path<String> path, String value, float minimumSimilarity) {
        Term term = new Term(path.getMetadata().getName(), value);
        return new QueryElement(new FuzzyQuery(term, minimumSimilarity));
    }

    /**
     * Create a fuzzy query
     *
     * @param path path
     * @param value value to match
     * @param minimumSimilarity a value between 0 and 1 to set the required similarity
     * @param prefixLength length of common (non-fuzzy) prefix
     * @return condition
     */
    public static BooleanExpression fuzzyLike(Path<String> path, String value,
            float minimumSimilarity, int prefixLength) {
        Term term = new Term(path.getMetadata().getName(), value);
        return new QueryElement(new FuzzyQuery(term, minimumSimilarity, prefixLength));
    }

    private LuceneExpressions() { }

}
