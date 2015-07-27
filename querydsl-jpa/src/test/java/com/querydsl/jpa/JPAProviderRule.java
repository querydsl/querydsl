package com.querydsl.jpa;

import java.lang.annotation.Annotation;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.Target;
import com.querydsl.core.testutil.EmptyStatement;

/**
 * @author tiwe
 *
 */
public class JPAProviderRule implements MethodRule {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        NoEclipseLink noEclipseLink = getAnnotation(method, NoEclipseLink.class);
        NoOpenJPA noOpenJPA = getAnnotation(method, NoOpenJPA.class);
        NoBatooJPA noBatooJPA = getAnnotation(method, NoBatooJPA.class);
        NoHibernate noHibernate = getAnnotation(method, NoHibernate.class);
        String mode = Mode.mode.get();
        if (mode == null) {
            return base;
        } else if (noEclipseLink != null && applies(noEclipseLink.value()) && mode.contains("-eclipselink")) {
            return EmptyStatement.DEFAULT;
        } else if (noOpenJPA != null && applies(noOpenJPA.value()) && mode.contains("-openjpa")) {
            return EmptyStatement.DEFAULT;
        } else if (noBatooJPA != null && applies(noBatooJPA.value()) && mode.contains("-batoo")) {
            return EmptyStatement.DEFAULT;
        } else if (noHibernate != null && applies(noHibernate.value()) && !mode.contains("-")) {
            return EmptyStatement.DEFAULT;
        } else {
            return base;
        }
    }

    private boolean applies(Target[] targets) {
        return targets.length == 0 || ImmutableSet.copyOf(targets).contains(Mode.target.get());
    }

    private <T extends Annotation> T getAnnotation(FrameworkMethod method, Class<T> clazz) {
        T rv = method.getMethod().getAnnotation(clazz);
        return rv != null ? rv : method.getMethod().getDeclaringClass().getAnnotation(clazz);
    }

}
