/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team).
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

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.github.classgraph.ClassGraph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.querydsl.core.types.Templates;

public class TemplatesTestBase {

    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();

    private final ClassGraph querydsl = new ClassGraph().enableClassInfo().acceptPackages(
            TemplatesTestBase.class.getPackage().getName());

    private final String modulePrefix = getClass().getPackage().getName();

    @Test
    public void default_instance() {
        List<Class<?>> templates = querydsl.scan().getSubclasses(Templates.class.getName()).loadClasses();
        Set<Class<?>> moduleSpecific = templates.stream().filter(MODULE_SPECIFIC).collect(Collectors.toSet());

        for (Class<?> template : moduleSpecific) {
            try {
                Templates defaultInstance = (Templates) template.getField("DEFAULT").get(null);
                errorCollector.checkThat(defaultInstance, instanceOf(template));
            } catch (Exception ex) {
                errorCollector.addError(ex);
            }
        }
    }

    private final Predicate<Class<?>> objectPredicate = o -> Pattern.matches("class " + modulePrefix + ".*", o.toString());
    private final Predicate<Class<?>> MODULE_SPECIFIC = objectPredicate.and(topLevelClass);

    private static final Predicate<Class<?>> topLevelClass = input -> !input.isAnonymousClass()
            && !input.isMemberClass();
}
