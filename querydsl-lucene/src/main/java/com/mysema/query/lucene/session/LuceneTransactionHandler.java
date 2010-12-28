package com.mysema.query.lucene.session;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LuceneTransactionHandler {

    private final Logger logger = LoggerFactory.getLogger(LuceneTransactionHandler.class);

    private LuceneSessionFactoryImpl sessionFactory;

    public LuceneTransactionHandler(LuceneSessionFactoryImpl sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Around("@annotation(luceneTransactionAnnotation)")
    public Object transactionalMethod(ProceedingJoinPoint joinPoint, LuceneTransaction annotation)
            throws Throwable {

        if (!LuceneSessionHolder.hasCurrentSession()) {

            if (logger.isDebugEnabled()) {
                logger.debug("Binding new session to thread");
            }

            LuceneSession session = sessionFactory.openSession(annotation.readOnly());
            LuceneSessionHolder.setCurrentSession(session);
        }

        LuceneSessionHolder.lease();
        try {
            return joinPoint.proceed();
        } finally {
            LuceneSessionHolder.release();
        }

    }

}
