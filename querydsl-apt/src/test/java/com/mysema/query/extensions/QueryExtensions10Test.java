package com.mysema.query.extensions;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.Interval;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EBooleanConst;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;

public class QueryExtensions10Test {
    
    /**
     * Adds a period filter
     */
    @QueryDelegate(Date.class)
    public static EBoolean period(PDate<Date> expr, Interval<Date> period) {
        return EBooleanConst.TRUE;
    }

    /**
     * Adds a timestamp period filter on a timestamp expression
     */
    @QueryDelegate(Timestamp.class)
    public static EBoolean period(PDateTime<Timestamp> expr, Interval<Timestamp> period) {
        return EBooleanConst.TRUE;
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
