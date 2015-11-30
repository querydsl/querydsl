package com.querydsl.jpa;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.Target;
import com.querydsl.core.testutil.EmptyStatement;

/**
 * @author tiwe
 *
 */
public class JPAProviderRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        NoEclipseLink noEclipseLink = description.getAnnotation(NoEclipseLink.class);
        NoOpenJPA noOpenJPA = description.getAnnotation(NoOpenJPA.class);
        NoBatooJPA noBatooJPA = description.getAnnotation(NoBatooJPA.class);
        NoHibernate noHibernate = description.getAnnotation(NoHibernate.class);
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
}
