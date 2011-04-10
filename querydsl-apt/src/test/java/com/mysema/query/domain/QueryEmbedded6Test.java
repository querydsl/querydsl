package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.EntityPathBase;

public class QueryEmbedded6Test {

    @QueryEntity
    public static class User {
        
        @QueryEmbedded
        List<User> list;
        
    }
    
    @Test
    public void EntityPathBase_is_SuperClass(){
        assertEquals(EntityPathBase.class, QQueryEmbedded6Test_User.class.getSuperclass());    
    }
    
    @Test
    public void User_list_any(){
        assertEquals(QQueryEmbedded6Test_User.class, QQueryEmbedded6Test_User.user.list.any().getClass());        
    }
    
}
