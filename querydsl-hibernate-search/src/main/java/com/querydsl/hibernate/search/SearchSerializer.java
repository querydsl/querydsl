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
package com.querydsl.hibernate.search;

import org.hibernate.search.annotations.Field;

import com.querydsl.lucene3.LuceneSerializer;
import com.querydsl.core.types.Path;

/**
 * SearchSerializer extends the LuceneSerializer to use {@link Field} annotation data from paths 
 * 
 * @author tiwe
 *
 */
public class SearchSerializer extends LuceneSerializer{

    public static final SearchSerializer DEFAULT = new SearchSerializer(false,true);

    /**
     * Create a new SearchSerializer instance
     * 
     * @param lowerCase
     * @param splitTerms
     */
    public SearchSerializer(boolean lowerCase, boolean splitTerms) {
        super(lowerCase, splitTerms);
    }

    @Override
    public String toField(Path<?> path) {
        if (path.getAnnotatedElement() != null) {
            Field fieldAnn = path.getAnnotatedElement().getAnnotation(Field.class);
            if (fieldAnn != null && fieldAnn.name().length() > 0) {
                return fieldAnn.name();
            }
        }
        return super.toField(path);
    }

}
