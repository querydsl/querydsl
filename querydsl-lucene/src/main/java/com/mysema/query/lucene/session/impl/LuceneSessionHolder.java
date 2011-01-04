package com.mysema.query.lucene.session.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryException;
import com.mysema.query.lucene.session.LuceneSession;
import com.mysema.query.lucene.session.LuceneSessionFactory;

/**
 * Holds the thread local sessions. This can handle several session factories
 * per thread.
 * 
 * @author laim
 */
public final class LuceneSessionHolder {

    private static final Logger logger = LoggerFactory.getLogger(LuceneSessionHolder.class);

    private static final ThreadLocal<Map<LuceneSessionFactory, LuceneSession>> sessions =
        new ThreadLocal<Map<LuceneSessionFactory, LuceneSession>>();

    private static final ThreadLocal<TransactionalScope> scope =
        new ThreadLocal<TransactionalScope>();

    private static class TransactionalScope {
        private int referenceCount = 0;

        private final boolean readOnly;

        private TransactionalScope(boolean readOnly) {
            this.readOnly = readOnly;
        }
    }

    private LuceneSessionHolder() {
    }

    public static boolean isTransactionalScope() {
        return scope.get() != null;
    }

    public static boolean hasCurrentSession(LuceneSessionFactory sessionFactory) {
        return getSessions().get(sessionFactory) != null;
    }

    public static LuceneSession getCurrentSession(LuceneSessionFactory sessionFactory) {
        return getSessions().get(sessionFactory);
    }

    public static void setCurrentSession(LuceneSessionFactory sessionFactory, LuceneSession session) {
        if (getSessions().containsKey(sessionFactory)) {
            throw new IllegalStateException(
                                            "Session factory has already bound a session to thread : "
                                                    + sessionFactory);
        }
        getSessions().put(sessionFactory, session);
    }

    private static Map<LuceneSessionFactory, LuceneSession> getSessions() {
        if (sessions.get() == null) {
            sessions.set(new HashMap<LuceneSessionFactory, LuceneSession>());
            
        }
        return sessions.get();
    }

    public static void release() {
        scope.get().referenceCount--;
        if (scope.get().referenceCount == 0) {
            try {
                for (LuceneSession session : getSessions().values()) {
                    try {
                        //System.out.println("session holder close");
                        session.close();
                    } catch (QueryException e) {
                        //System.out.println("failed to close session " + e.getCause().getMessage());
                        logger.error("Failed to close session", e);
                    }
                }
            } finally {
                sessions.remove();
                scope.remove();
            }
        }
    }

    public static void lease(boolean readOnly) {
        if (scope.get() == null) {
            scope.set(new TransactionalScope(readOnly));
        }
        scope.get().referenceCount++;
    }

    public static boolean getReadOnly() {
        Assert.notNull(scope.get(), "No transactional scope");
        return scope.get().readOnly;
    }

}
