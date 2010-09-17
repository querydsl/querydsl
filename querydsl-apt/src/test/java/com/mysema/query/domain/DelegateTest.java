package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.BooleanConstant;
import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.expr.BooleanExpression;

public class DelegateTest {

    @QuerySupertype
    public static class Identifiable {

        long id;

    }

    @QueryEntity
    public static class User extends Identifiable{

        String name;

        User managedBy;

    }

    @QueryEntity
    public static class SimpleUser extends User{

    }

    @QueryEntity
    public static class SimpleUser2 extends SimpleUser{

    }

    @QueryDelegate(User.class)
    public static BooleanExpression isManagedBy(QDelegateTest_User user, User other){
        return BooleanConstant.TRUE;
    }

    @QueryDelegate(User.class)
    public static BooleanExpression isManagedBy(QDelegateTest_User user, QDelegateTest_User other){
        return BooleanConstant.TRUE;
    }

    @QueryDelegate(User.class)
    public static BooleanExpression simpleMethod(QDelegateTest_User user){
        return BooleanConstant.TRUE;
    }

//    @QueryDelegate(DelegateTest.User.class)
//    public static PString getName(QDelegateTest_User user){
//        return user.name;
//    }

    @Test
    public void testUser(){
        QDelegateTest_User user = QDelegateTest_User.user;
        assertNotNull(user.isManagedBy(new User()));
        assertNotNull(user.isManagedBy(user));
        assertNotNull(user.simpleMethod());

        assertEquals(user.name, user.getName());
    }

    @Test
    public void testSimpleUser(){
        QDelegateTest_SimpleUser user = QDelegateTest_SimpleUser.simpleUser;
        assertNotNull(user.isManagedBy(new User()));
        assertNotNull(user.isManagedBy(user._super));

        assertEquals(user.name, user.getName());
    }

    @Test
    public void testSimpleUser2(){
        QDelegateTest_SimpleUser2 user = QDelegateTest_SimpleUser2.simpleUser2;
        assertNotNull(user.isManagedBy(new User()));
        assertNotNull(user.isManagedBy(user._super._super));

        assertEquals(user.name, user.getName());
    }

}
