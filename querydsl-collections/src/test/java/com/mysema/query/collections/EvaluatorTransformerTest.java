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
import com.mysema.query.animal.QCat;

public class EvaluatorTransformerTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void test(){
        DefaultEvaluatorFactory evaluatorFactory = new DefaultEvaluatorFactory(ColQueryTemplates.DEFAULT);
        QCat cat = QCat.cat;
        Evaluator projectionEvaluator = evaluatorFactory.create(Collections.singletonList(cat), cat.name);
        EvaluatorTransformer transformer = new EvaluatorTransformer(projectionEvaluator);
        
        Cat c = new Cat("Kitty");
        assertEquals("Kitty", transformer.transform(c));        
    }

}
