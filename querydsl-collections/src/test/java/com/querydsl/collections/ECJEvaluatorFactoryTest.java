package com.querydsl.collections;

import org.junit.Test;

import com.querydsl.codegen.utils.ECJEvaluatorFactory;

public class ECJEvaluatorFactoryTest extends AbstractQueryTest {

    @Test
    public void evaluator_factory() {
        DefaultEvaluatorFactory evaluatorFactory = new DefaultEvaluatorFactory(
                CollQueryTemplates.DEFAULT,
                new ECJEvaluatorFactory(getClass().getClassLoader()));
        QueryEngine queryEngine = new DefaultQueryEngine(evaluatorFactory);
        CollQuery<?> query = new CollQuery<Void>(queryEngine);
        query.from(cat, cats).select(cat.name).fetch();
    }

}
