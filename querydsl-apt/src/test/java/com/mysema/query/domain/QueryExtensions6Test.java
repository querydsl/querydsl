package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;

public class QueryExtensions6Test {

    @QueryEntity
    public static class User {

        private User managedBy;

        public User getManagedBy() {
            return managedBy;
        }

        public void setManagedBy(User managedBy) {
            this.managedBy = managedBy;
        }

    }

    @QueryExtensions(User.class)
    public static interface UserMethods{

        @QueryMethod("{0}.managedBy = {1}")
        boolean isManagedBy(User other);

    }

    @Test
    public void test(){
        QQueryExtensions6Test_User user = QQueryExtensions6Test_User.user;
        QQueryExtensions6Test_User other = new QQueryExtensions6Test_User("other");
        assertEquals("user.managedBy = other", user.isManagedBy(other).toString());

        System.out.println(user.isManagedBy(new User()));
    }

}
