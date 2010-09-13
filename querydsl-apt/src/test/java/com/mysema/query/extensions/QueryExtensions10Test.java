package com.mysema.query.extensions;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanConstant;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;

public class QueryExtensions10Test {
    
    public interface Interval<T>{
        
    }
    
    /**
     * Adds a period filter
     */
    @QueryDelegate(Date.class)
    public static BooleanExpression period(DatePath<Date> expr, Interval<Date> period) {
        return BooleanConstant.TRUE;
    }

    /**
     * Adds a timestamp period filter on a timestamp expression
     */
    @QueryDelegate(Timestamp.class)
    public static BooleanExpression period(DateTimePath<Timestamp> expr, Interval<Timestamp> period) {
        return BooleanConstant.TRUE;
    }
    
    @QueryEmbeddable
    public class ImageData {
        
    }
    
    @QueryEntity
    public class User {
        
    }
    
    @QuerySupertype
    public abstract class Image {

        ImageData thumbnail;

        ImageData image;

        ImageData mediumImage;
        
        String contentType;
    }

    @QueryEntity
    public abstract class CustomImage extends Image {

        Timestamp creationDate;
    }

    @QueryEntity
    public class UserCustomImage extends CustomImage {

        User user;
        
    }
    
    @Test
    public void test(){
        // TODO
    }

}
