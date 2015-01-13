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

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class FilteringTestRunner extends BlockJUnit4ClassRunner {

    private boolean run = true;

    public FilteringTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        ResourceCheck rc = klass.getAnnotation(ResourceCheck.class);
        if (rc != null) {
            run = klass.getResourceAsStream(rc.value()) != null;
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        if (run) {
            super.run(notifier);
        }
    }

}
