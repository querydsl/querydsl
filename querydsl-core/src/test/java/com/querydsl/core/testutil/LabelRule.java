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

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.querydsl.core.Target;

public class LabelRule implements MethodRule{

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        Label label = target.getClass().getAnnotation(Label.class);
        boolean run = true;
        if (label != null) {
            run = isExecuted(method.getMethod(), label.value()); 
        }
        return run ? base : EmptyStatement.DEFAULT;
    }
    
    private boolean isExecuted(Method method, Target target) {
        ExcludeIn ex = method.getAnnotation(ExcludeIn.class);
        // excluded in given targets
        if (ex != null && Arrays.asList(ex.value()).contains(target)) {
            return false;
        }
        // included only in given targets
        IncludeIn in = method.getAnnotation(IncludeIn.class);
        if (in != null && !Arrays.asList(in.value()).contains(target)) {
            return false;
        }
        return true;
    }

}
