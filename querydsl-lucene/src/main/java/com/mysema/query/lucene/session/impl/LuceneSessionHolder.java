package com.mysema.query.lucene.session.impl;

import com.mysema.query.lucene.session.LuceneSession;
import com.mysema.query.lucene.session.NoSessionBoundException;


/**
 * Holds the thread local session
 * 
 * @author laimw
 */
public final class LuceneSessionHolder {

    private static final ThreadLocal<LuceneSessionRef> currentSessionRef =
        new ThreadLocal<LuceneSessionRef>();

    private static class LuceneSessionRef {
        private LuceneSession session;

        private int referenceCount = 0;

        LuceneSessionRef(LuceneSession session) {
            this.session = session;
        }
    }

    private LuceneSessionHolder() {
    }

    public static boolean hasCurrentSession() {
        return currentSessionRef.get() != null;
    }

    public static LuceneSession getCurrentSession() {
        return getSessionRef().session;
    }

    public static void setCurrentSession(LuceneSession session) {
        LuceneSessionRef ref = new LuceneSessionRef(session);
        currentSessionRef.set(ref);
    }

    public static void release() {
        LuceneSessionRef ref = getSessionRef();
        ref.referenceCount--;
        if (ref.referenceCount == 0) {
            try {
                ref.session.close();
            } finally {
                currentSessionRef.remove();
            }
        }
    }

    public static void lease() {
        getSessionRef().referenceCount++;
    }

    private static LuceneSessionRef getSessionRef() {
        if (!hasCurrentSession()) {
            throw new NoSessionBoundException("There is no session bound to local thread");
        }
        return currentSessionRef.get();
    }

}
