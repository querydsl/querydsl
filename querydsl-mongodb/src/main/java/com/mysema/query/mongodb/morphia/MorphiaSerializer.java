package com.mysema.query.mongodb.morphia;

import com.google.code.morphia.annotations.Property;
import com.mysema.query.mongodb.MongodbSerializer;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathType;

/**
 * MorphiaSerializer extends MongodbSerializer with Morphia specific annotation handling
 *
 * @author tiwe
 *
 */
public class MorphiaSerializer extends MongodbSerializer{

    public static final MorphiaSerializer DEFAULT = new MorphiaSerializer();

    @Override
    protected String getKeyForPath(Path<?> expr, PathMetadata<?> metadata) {
        if (metadata.getPathType() == PathType.PROPERTY && expr.getAnnotatedElement().isAnnotationPresent(Property.class)){
            return expr.getAnnotatedElement().getAnnotation(Property.class).value();
        }else{
            return super.getKeyForPath(expr, metadata);
        }
    }

}
