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
package com.mysema.query.apt;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.domain.EntityTest;

@Ignore
public class GenericExporterTest {

    private GenericExporter exporter;

    @Before
    public void setUp() {
        exporter = new GenericExporter();
    }

    @Test
    public void Export() throws IOException {
        if (new File("target/gen1").exists()){
            FileUtils.cleanDirectory(new File("target/gen1"));
        }

        exporter.setTargetFolder(new File("target/gen1"));
        exporter.export(EntityTest.class.getPackage());

        Set<String> skip = new HashSet<String>();

        // delegate tests
        skip.add("QDelegate2Test_Entity.java");
        skip.add("QDelegateTest_SimpleUser.java");
        skip.add("QDelegateTest_SimpleUser2.java");
        skip.add("QDelegateTest_User.java");

        // projection tests
        skip.add("QQueryProjectionTest_EntityWithProjection.java");

        int total = 0;
        Set<String> failures = new TreeSet<String>();
        for (File file : new File("target/gen1/com/mysema/query/domain").listFiles()){
            if (file.isDirectory() || skip.contains(file.getName())){
                continue;
            }
            File result = new File("target/generated-test-sources/java/com/mysema/query/domain", file.getName());
            if (!result.exists()){
                continue;
            }
            String result1 = FileUtils.readFileToString(file, "UTF-8");
            String result2 = FileUtils.readFileToString(result, "UTF-8");
            if (!result1.equals(result2)){
                failures.add(file.getName());
            }
            total++;
        }

        if (!failures.isEmpty()){
            for (String failure : failures){
                System.err.println(failure);
            }
            fail("Failed with " + failures.size() + " failures of " + total);
        }
    }

}
