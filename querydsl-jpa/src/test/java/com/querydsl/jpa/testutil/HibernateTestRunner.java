/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa.testutil;

import static com.google.common.base.Verify.verify;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.rules.MethodRule;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.querydsl.jpa.Mode;
import com.querydsl.jpa.HibernateTest;
import com.querydsl.jpa.domain.Domain;

/**
 * @author tiwe
 *
 */
public class HibernateTestRunner extends BlockJUnit4ClassRunner {

    private SessionFactory sessionFactory;

    private Session session;

    private boolean isDerby = false;

    public HibernateTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<MethodRule> rules(Object test) {
        verify(test instanceof HibernateTest, "In order to use the %s for %s, it should (directly or indirectly) implement %s",
                HibernateTestRunner.class.getSimpleName(), test.getClass(), HibernateTest.class);

        List<MethodRule> rules = super.rules(test);
        rules.add(new MethodRule() {
            @Override
            public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
                return new Statement() {
                    @Override
                    public void evaluate() throws Throwable {
                        ((HibernateTest) target).setSession(session);
                        base.evaluate();
                    }
                };
            }

        });
        return rules;
    }

    @Override
    public void run(final RunNotifier notifier) {
        try {
            start();
            super.run(notifier);
        } catch (Exception e) {
            e.printStackTrace();
            Failure failure = new Failure(Description.createSuiteDescription(getTestClass().getJavaClass()), e);
            notifier.fireTestFailure(failure);
        } finally {
            shutdown();
        }
    }

    private void start() throws Exception {
        Configuration cfg = new Configuration();
        for (Class<?> cl : Domain.classes) {
            cfg.addAnnotatedClass(cl);
        }
        String mode = Mode.mode.get() + ".properties";
        isDerby = mode.contains("derby");
        if (isDerby) {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        }
        Properties props = new Properties();
        InputStream is = HibernateTestRunner.class.getResourceAsStream(mode);
        if (is == null) {
            throw new IllegalArgumentException("No configuration available at classpath:" + mode);
        }
        props.load(is);
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
            .applySettings(props)
            .build();
        cfg.setProperties(props);
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    private void shutdown() {
        if (session != null) {
            try {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            } finally {
                session.close();
                session = null;
            }
        }

        if (sessionFactory != null) {
            sessionFactory.getCache().evictEntityRegions();
            sessionFactory.close();
            sessionFactory = null;
        }

        // clean shutdown of derby
        if (isDerby) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                if (!e.getMessage().equals("Derby system shutdown.")) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
