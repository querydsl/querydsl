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
package com.querydsl.core.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemplateTest {

    @Test
    public void test() {
        TemplateFactory factory = new TemplateFactory('\\');
        match("[0, ' + ', 1, ' + ', 2]", factory.create("{0} + {1} + {2}"));
        match("['blah ', 0, ' + ', 1, ' + ', 2, ' XXX']", factory.create("blah {0} + {1} + {2} XXX"));
        match("['+', 1]",                factory.create("+{1}"));
        match("[1, '.method()']",        factory.create("{1}.method()"));
        match("[0, '.get(', 1, ')']",    factory.create("{0}.get({1})"));
        match("[0, '.', 1s]",            factory.create("{0}.{1s}"));
    }

    @Test
    public void like() {
        TemplateFactory factory = new TemplateFactory('\\');
        match("[0]",                     factory.create("{0%}"));
        match("[0]",                     factory.create("{%0}"));
        match("[0]",                     factory.create("{%0%}"));

        match("[0]",                     factory.create("{0%%}"));
        match("[0]",                     factory.create("{%%0}"));
        match("[0]",                     factory.create("{%%0%%}"));
    }

    private void match(String string, Template template) {
        assertEquals(string, template.getElements().toString());
    }

}

