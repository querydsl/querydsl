package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EBooleanConst;
import com.mysema.query.types.path.PString;

public class DelegateTest {
    
    @QueryEntity
    public static class User{
        
        String name;
        
        User managedBy;
        
    }
    
    @QueryDelegate(User.class)
    public static EBoolean isManagedBy(QDelegateTest_User user, User other){
        return EBooleanConst.TRUE;
    }
    
    @QueryDelegate(User.class)
    public static EBoolean isManagedBy(QDelegateTest_User user, QDelegateTest_User other){
        return EBooleanConst.TRUE;
    }
    
    @QueryDelegate(User.class)
    public static EBoolean simpleMethod(QDelegateTest_User user){
        return EBooleanConst.TRUE;
    }
    
    @QueryDelegate(User.class)
    public static PString getName(QDelegateTest_User user){
        return user.name;
    }
    
    @Test
    public void test(){        
        QDelegateTest_User user = QDelegateTest_User.user;
        assertNotNull(user.isManagedBy(new User()));
        assertNotNull(user.isManagedBy(user));
        assertNotNull(user.simpleMethod());
        
        assertEquals(user.name, user.getName());
    }

}
