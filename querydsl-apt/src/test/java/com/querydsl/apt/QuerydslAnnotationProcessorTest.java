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
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.querydsl.apt.hibernate.HibernateAnnotationProcessor;
import com.querydsl.apt.jdo.JDOAnnotationProcessor;
import com.querydsl.apt.jpa.JPAAnnotationProcessor;
import com.querydsl.apt.roo.RooAnnotationProcessor;

public class QuerydslAnnotationProcessorTest extends AbstractProcessorTest {

    private static final String PACKAGE_PATH = "src/test/java/com/mysema/querydsl/domain/";

    private static final List<String> CLASSES = getFiles(PACKAGE_PATH);

    @Test
    public void Process() throws IOException {
        File file = new File(PACKAGE_PATH, "AbstractEntityTest.java");
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(file.getPath()),"qdsl");
    }

    @Test
    public void Process_MonitoredCompany() throws IOException {
        String path = new File(PACKAGE_PATH, "MonitoredCompany.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"MonitoredCompany");
    }

    @Test
    public void Process_Inheritance3() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/inheritance/Inheritance3Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"Inheritance3Test");
    }

    @Test
    public void Process_Inheritance8() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/inheritance/Inheritance8Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"Inheritance8Test");
    }

    @Test
    public void Process_QueryEmbedded3() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/QueryEmbedded3Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"QueryEmbedded3Test");
    }

    @Test
    public void Process_QueryEmbedded4() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/QueryEmbedded4Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"QueryEmbedded4Test");
    }

    @Test
    public void Process_Delegate() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/DelegateTest.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"DelegateTest");
    }

    @Test
    public void Process_AbstractClasses() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/AbstractClassesTest.java").getPath();
        process(JPAAnnotationProcessor.class, Collections.singletonList(path),"AbstractClassesTest");
    }

    @Test
    public void Process_AbstractClasses2() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/AbstractClasses2Test.java").getPath();
        process(JPAAnnotationProcessor.class, Collections.singletonList(path),"abstractClasses2");
    }

    @Test
    public void Process_GenericSignature() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/GenericSignatureTest.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"GenericSignatureTest");
    }

    @Test
    public void Process_AbstractProperties2Test() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/AbstractProperties2Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"AbstractProperties2Test");
    }

    @Test
    public void Process_Inheritance2Test() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/inheritance/Inheritance2Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"InheritanceTest2");
    }

    @Test
    public void Process_EntityInheritanceTest() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/EntityInheritanceTest.java").getPath();
        process(JPAAnnotationProcessor.class, Collections.singletonList(path),"EntityInheritanceTest");
    }

    @Test
    public void Process_Enum2Test() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/Enum2Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"Enum2Test");
    }

    @Test
    public void Process_ExternalEntityTest() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/ExternalEntityTest.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"ExternalEntityTest");
    }

    @Test
    public void Process_Generic13Test() throws IOException {
        String path = new File("src/test/java/com/mysema/querydsl/domain/Generic13Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"Generic13Test");
    }

    @Test
    public void QuerydslAnnotationProcessor() throws IOException {
        process(QuerydslAnnotationProcessor.class, CLASSES, "querydsl");
    }

    @Test
    public void JPAAnnotationProcessor() throws IOException {
        process(JPAAnnotationProcessor.class, CLASSES, "jpa");
    }

    @Test
    public void HibernateAnnotationProcessor() throws IOException {
        process(HibernateAnnotationProcessor.class, CLASSES, "hibernate");
    }

    @Test
    public void JDOAnnotationProcessor() throws IOException {
        process(JDOAnnotationProcessor.class, CLASSES, "jdo");
    }

    @Test
    public void RooAnnotationProcessor() throws IOException {
        process(RooAnnotationProcessor.class, CLASSES, "roo");

        assertTrue(new File("target/roo/com/mysema/querydsl/domain/QRooEntities_MyEntity.java").exists());
    }

}
