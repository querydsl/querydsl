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
package com.querydsl.core;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.reflections.ReflectionUtils.*;

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.reflections.Reflections;

import com.google.common.base.Predicate;
import com.querydsl.core.types.Templates;

public class TemplatesTestBase {

    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();

    private final Reflections querydsl = new Reflections(
            TemplatesTestBase.class.getPackage().getName());

    private final String modulePrefix = getClass().getPackage().getName();

    @Test
    public void Default_Instance() {
        Set<Class<? extends Templates>> templates = querydsl.getSubTypesOf(Templates.class);
        Set<Class<? extends Templates>> moduleSpecific = getAll(templates, topLevelClass,
                withPattern("class " + modulePrefix + ".*"));

        for (Class<? extends Templates> template : moduleSpecific) {
            try {
                Templates defaultInstance = (Templates) template.getField("DEFAULT").get(null);
                errorCollector.checkThat(defaultInstance, instanceOf(template));
            } catch (Exception ex) {
                errorCollector.addError(ex);
            }
        }
    }
    private static final Predicate<Class<?>> topLevelClass = new Predicate<Class<?>>() {

        @Override
        public boolean apply(Class<?> input) {
            return !input.isAnonymousClass()
                    && !input.isMemberClass();
        }
    };
}
