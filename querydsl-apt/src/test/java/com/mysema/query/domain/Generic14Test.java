package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic14Test {

    @Entity
    public static class UserAccount extends BaseReferencablePersistable<UserAccount, Long> {

        public UserAccount() {
            super(UserAccount.class);
        }

    }

    @MappedSuperclass
    public static abstract class BaseReferencablePersistable<T, PK extends Serializable> extends BasePersistable<PK> {

        private Class<T> entityClass;

        public BaseReferencablePersistable(Class<T> entityClass) {
            this.entityClass = entityClass;
        }

    }

    @MappedSuperclass
    public static class BasePersistable<T extends Serializable> extends AbstractPersistable<T> implements UpdateInfo {

        private T id;

        @Override
        public T getId() {
            return id;
        }

    }

    @MappedSuperclass
    public static abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

    }

    public interface Persistable<T> {

        T getId();

    }

    public interface UpdateInfo {

    }

    @Test
    public void test() {
        assertNotNull(QGeneric14Test_UserAccount.userAccount);
        assertNotNull(QGeneric14Test_BaseReferencablePersistable.baseReferencablePersistable);
        assertNotNull(QGeneric14Test_BasePersistable.basePersistable);
        assertNotNull(QGeneric14Test_AbstractPersistable.abstractPersistable);
        assertTrue(QGeneric14Test_UserAccount.userAccount.id
                .getType().isAssignableFrom(Long.class));
    }
}
