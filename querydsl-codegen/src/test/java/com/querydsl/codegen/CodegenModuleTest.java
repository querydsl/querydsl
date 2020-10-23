/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Generated;

import org.junit.Test;

public class CodegenModuleTest {

    private final CodegenModule module = new CodegenModule();

    @Test
    public void defaultPrefix() {
        assertEquals("Q", module.get(String.class, CodegenModule.PREFIX));
    }

    @Test
    public void typeMappings() {
        assertNotNull(module.get(TypeMappings.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_with_unknown_key() {
        module.get(String.class, "XXX");
    }

    @Test
    public void defaultGeneratedClass() {
        String o = module.get(String.class, CodegenModule.GENERATED_ANNOTATION_CLASS);
        assertEquals(o, Generated.class.getName());
    }

    @Test
    public void javadocSuffixForBeanSerializerOverloadedConstructorInjection() {
        String o = module.get(String.class, CodegenModule.JAVADOC_SUFFIX);
        assertEquals(o, BeanSerializer.DEFAULT_JAVADOC_SUFFIX);
    }

}
