/*
 * Copyright 2014 Timo Westkämper.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.testutil;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

/**
 *
 * @author Shredder121
 */
public class TravisFoldListener extends RunListener {

    private static final long ID = System.currentTimeMillis();

    @Override
    public void testRunStarted(Description description) throws Exception {
        System.out.println("travis_fold:start:tests" + ID);
        System.out.println("Running tests");
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        System.out.println("travis_fold:end:tests" + ID);
    }

}
