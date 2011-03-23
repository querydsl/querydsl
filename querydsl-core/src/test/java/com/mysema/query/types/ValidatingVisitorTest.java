package com.mysema.query.types;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.template.SimpleTemplate;

@SuppressWarnings("unchecked")
public class ValidatingVisitorTest {

    private final Set<Expression<?>> known = new HashSet<Expression<?>>();

    private final ValidatingVisitor validator = new ValidatingVisitor(known);

    @Before
    public void setUp(){
        known.add(new PathImpl(Object.class, "path"));
    }

    @Test
    public void VisitConstantOfQVoid() {
        validator.visit(ConstantImpl.create("XXX"), null);
    }

    @Test
    public void VisitFactoryExpressionOfQVoid() {
        validator.visit(new QBean(Object.class, new PathImpl(String.class, "path")), null);
    }

    @Test
    public void VisitOperationOfQVoid() {
        validator.visit((Operation)new SimplePath(Object.class, "path").isNull(), null);
    }

    @Test
    public void VisitParamExpressionOfQVoid() {
        validator.visit(new Param(Object.class, "prop"), null);
    }

    @Test
    public void VisitPathOfQVoid() {
        validator.visit(new PathImpl(Object.class, "path"), null);
    }

    @Test
    public void VisitSubQueryExpressionOfQVoid() {
        validator.visit(new SubQueryExpressionImpl(Object.class, new DefaultQueryMetadata()), null);
    }

    @Test
    public void VisitTemplateExpressionOfQVoid() {
        validator.visit((TemplateExpression)SimpleTemplate.create(Object.class, "XXX"), null);
    }

}
