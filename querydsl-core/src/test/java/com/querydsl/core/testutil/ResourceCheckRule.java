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
package com.querydsl.core.testutil;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class ResourceCheckRule implements MethodRule{

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        Class<?> testClass = target.getClass();
        ResourceCheck rc = testClass.getAnnotation(ResourceCheck.class);
        boolean run = true;
        if (rc != null) {
            run = testClass.getResourceAsStream(rc.value()) != null;
        }
        return run ? base : EmptyStatement.DEFAULT;
    }

}
