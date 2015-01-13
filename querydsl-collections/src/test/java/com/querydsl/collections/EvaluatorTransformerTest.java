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
package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.mysema.codegen.Evaluator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;

public class EvaluatorTransformerTest {

    private QueryMetadata metadata = new DefaultQueryMetadata();

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        DefaultEvaluatorFactory evaluatorFactory = new DefaultEvaluatorFactory(CollQueryTemplates.DEFAULT);
        QCat cat = QCat.cat;
        Evaluator projectionEvaluator = evaluatorFactory.create(metadata, Collections.singletonList(cat), cat.name);
        EvaluatorFunction transformer = new EvaluatorFunction(projectionEvaluator);

        Cat c = new Cat("Kitty");
        assertEquals("Kitty", transformer.apply(c));
    }

}
