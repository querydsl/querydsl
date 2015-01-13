package com.querydsl.collections;

import com.mysema.codegen.ECJEvaluatorFactory;
import org.junit.Test;

public class ECJEvaluatorFactoryTest extends AbstractQueryTest {

    @Test
    public void Evaluator_Factory() {
        DefaultEvaluatorFactory evaluatorFactory = new DefaultEvaluatorFactory(
                CollQueryTemplates.DEFAULT,
                new ECJEvaluatorFactory(getClass().getClassLoader()));
        QueryEngine queryEngine = new DefaultQueryEngine(evaluatorFactory);
        CollQuery query = new CollQuery(queryEngine);
        query.from(cat, cats).list(cat.name);
    }

}
