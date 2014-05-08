package com.mysema.query.domain;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

import org.junit.Test;

public class Generic14Test {

    @Entity
    public static class UserAccount extends BaseReferencablePersistable<Long> {

        @Override
        public Reference getReference() throws NamingException {
            return null;
        }
    }

    @MappedSuperclass
    public static abstract class BaseReferencablePersistable<PK extends Serializable> extends BasePersistable<PK> implements Referenceable {

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

    }
}
