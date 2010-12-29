package com.mysema.query.lucene.session.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.lucene.session.LuceneTransactional;

@Aspect
public class LuceneTransactionHandler {

    private static final Logger logger = LoggerFactory.getLogger(LuceneTransactionHandler.class);

    @Around("@annotation(annotation)")
    public Object transactionalMethod(ProceedingJoinPoint joinPoint, LuceneTransactional annotation)
            throws Throwable {

        if(logger.isDebugEnabled()) {
            logger.debug("Starting LuceneTransactional method");
        }
        
        LuceneSessionHolder.lease(annotation.readOnly());
        try {
            return joinPoint.proceed();
        } finally {
            LuceneSessionHolder.release();
        }

    }

}
