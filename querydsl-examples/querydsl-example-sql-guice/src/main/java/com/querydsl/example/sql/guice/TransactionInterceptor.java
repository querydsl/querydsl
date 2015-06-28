package com.querydsl.example.sql.guice;

import java.lang.reflect.Method;
import java.sql.Connection;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TransactionInterceptor implements MethodInterceptor {
    @Inject
    private ConnectionContext context;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Transactional annotation = method.getAnnotation(Transactional.class);
        if (annotation == null || context.getConnection() != null) {
            return invocation.proceed();
        }
        Connection connection = context.getConnection(true);
        connection.setAutoCommit(false);
        try {
            Object rv = invocation.proceed();
            connection.commit();
            return rv;
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
            context.removeConnection();
        }
    }
}
