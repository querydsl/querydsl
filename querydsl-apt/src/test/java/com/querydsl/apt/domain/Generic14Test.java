package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

import com.querydsl.apt.domain.QGeneric14Test_AbstractPersistable;
import com.querydsl.apt.domain.QGeneric14Test_BasePersistable;
import com.querydsl.apt.domain.QGeneric14Test_BaseReferencablePersistable;
import com.querydsl.apt.domain.QGeneric14Test_UserAccount;

public class Generic14Test extends AbstractTest {

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
    public void test() throws IllegalAccessException, NoSuchFieldException {
        assertNotNull(QGeneric14Test_AbstractPersistable.abstractPersistable);

        start(QGeneric14Test_BasePersistable.class, QGeneric14Test_BasePersistable.basePersistable);
        matchType(Serializable.class, "id");

        start(QGeneric14Test_BaseReferencablePersistable.class, QGeneric14Test_BaseReferencablePersistable.baseReferencablePersistable);
        matchType(Class.class, "entityClass");
        matchType(Serializable.class, "id");

        start(QGeneric14Test_UserAccount.class, QGeneric14Test_UserAccount.userAccount);
        matchType(Long.class, "id");
    }
}
