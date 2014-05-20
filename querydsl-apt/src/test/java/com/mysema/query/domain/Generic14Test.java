package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class Generic14Test {

    @Entity
    public static class UserAccount extends BaseReferencablePersistable<Long> {

    }

    @MappedSuperclass
    public static abstract class BaseReferencablePersistable<PK extends Serializable> extends BasePersistable<PK> {

    }

    @MappedSuperclass
    public static class BasePersistable<T extends Serializable> extends AbstractPersistable<T> implements UpdateInfo {

    }

    @MappedSuperclass
    public static abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

    }

    public interface Persistable<T> {

    }

    public interface UpdateInfo {

    }

    @Test
    public void test() {
        assertNotNull(QGeneric14Test_UserAccount.userAccount);
        assertNotNull(QGeneric14Test_BaseReferencablePersistable.baseReferencablePersistable);
        assertNotNull(QGeneric14Test_BasePersistable.basePersistable);
        assertNotNull(QGeneric14Test_AbstractPersistable.abstractPersistable);
    }
}
