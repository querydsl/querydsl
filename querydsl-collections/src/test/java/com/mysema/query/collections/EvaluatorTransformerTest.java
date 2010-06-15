/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.mysema.codegen.Evaluator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;

public class EvaluatorTransformerTest {
    
    private QueryMetadata metadata = new DefaultQueryMetadata();
    
    @SuppressWarnings("unchecked")
    @Test
    public void test(){
        DefaultEvaluatorFactory evaluatorFactory = new DefaultEvaluatorFactory(ColQueryTemplates.DEFAULT);
        QCat cat = QCat.cat;
        Evaluator projectionEvaluator = evaluatorFactory.create(metadata, Collections.singletonList(cat), cat.name);
        EvaluatorTransformer transformer = new EvaluatorTransformer(projectionEvaluator);
        
        Cat c = new Cat("Kitty");
        assertEquals("Kitty", transformer.transform(c));        
    }

}
