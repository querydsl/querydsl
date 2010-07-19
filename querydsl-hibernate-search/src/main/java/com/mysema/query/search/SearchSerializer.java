/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import org.hibernate.search.annotations.Field;

import com.mysema.query.lucene.LuceneSerializer;
import com.mysema.query.types.Path;

/**
 * SearchSerializer extends the LuceneSerializer to use {@link Field} annotation data from paths 
 * 
 * @author tiwe
 *
 */
public class SearchSerializer extends LuceneSerializer{

    public static final SearchSerializer DEFAULT = new SearchSerializer(false,true);

    public SearchSerializer(boolean lowerCase, boolean splitTerms){
        super(lowerCase, splitTerms);
    }

    @Override
    public String toField(Path<?> path) {
        if (path.getAnnotatedElement() != null){
            Field fieldAnn = path.getAnnotatedElement().getAnnotation(Field.class);
            if (fieldAnn != null && fieldAnn.name().length() > 0){
                return fieldAnn.name();
            }
        }
        return super.toField(path);
    }

}
