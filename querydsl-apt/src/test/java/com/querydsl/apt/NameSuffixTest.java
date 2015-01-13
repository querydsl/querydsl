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
package com.querydsl.apt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NameSuffixTest extends AbstractProcessorTest {
    
    private static final String packagePath = "src/test/java/com/mysema/querydsl/domain/";

    @Test
    public void ProcessAll() throws IOException {
        // works only in Eclipse for the moment
        List<String> classes = getFiles(packagePath);

        // default Processor
        process(QuerydslAnnotationProcessor.class, classes,"suffix");
        
        assertTrue(new File("target/suffix/com/mysema/querydsl/domain/QAnimalTest_AnimalType.java").exists());
    }
    
    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.suffix=Type");
    }
    
}
