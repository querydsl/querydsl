package com.mysema.query.search;

import org.hibernate.search.annotations.Field;

import com.mysema.query.lucene.LuceneSerializer;
import com.mysema.query.types.Path;

public class SearchSerializer extends LuceneSerializer{

    public SearchSerializer(boolean lowerCase) {
        super(lowerCase);
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
