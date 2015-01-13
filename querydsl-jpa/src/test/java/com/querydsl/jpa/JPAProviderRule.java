package com.querydsl.jpa;

import java.lang.annotation.Annotation;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.querydsl.core.testutil.EmptyStatement;

/**
 * @author tiwe
 *
 */
public class JPAProviderRule implements MethodRule {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        boolean noEclipseLink = hasAnnotation(method, NoEclipseLink.class);
        boolean noOpenJPA = hasAnnotation(method, NoOpenJPA.class);
        boolean noBatooJPA = hasAnnotation(method, NoBatooJPA.class);
        boolean noHibernate = hasAnnotation(method, NoHibernate.class);
        String mode = Mode.mode.get();
        if (mode == null) {
            return base;
        } else if (noEclipseLink && mode.contains("-eclipselink")) {
            return EmptyStatement.DEFAULT;
        } else if (noOpenJPA && mode.contains("-openjpa")) {
            return EmptyStatement.DEFAULT;
        } else if (noBatooJPA && mode.contains("-batoo")) {
            return EmptyStatement.DEFAULT;
        } else if (noHibernate && !mode.contains("-")) {
            return EmptyStatement.DEFAULT;
        } else {
            return base;
        }
    }
    
    private <T extends Annotation> boolean hasAnnotation(FrameworkMethod method, Class<T> clazz) {
        return method.getMethod().isAnnotationPresent(clazz)
                || method.getMethod().getDeclaringClass().isAnnotationPresent(clazz);
    }

}
