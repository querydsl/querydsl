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
package com.querydsl.apt.domain;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QInterfaceTypeTest_InterfaceType;
import com.querydsl.apt.domain.QInterfaceTypeTest_InterfaceType3;
import com.querydsl.apt.domain.QInterfaceTypeTest_InterfaceType5;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.NumberPath;

public class InterfaceTypeTest extends AbstractTest {

    @QueryEntity
    public interface InterfaceType {
        InterfaceType getRelation();

        List<InterfaceType> getRelation2();

        List<? extends InterfaceType> getRelation3();

        int getRelation4();

        String getProp();
    }

    @QueryEntity
    public interface InterfaceType2 {

        public String getProp2();

    }

    @QueryEntity
    public interface InterfaceType3 extends InterfaceType, InterfaceType2 {

        public String getProp3();

    }

    @QueryEntity
    public interface InterfaceType4 {

        public String getProp4();

    }

    @QueryEntity
    public interface InterfaceType5 extends InterfaceType3, InterfaceType4 {

        public String getProp5();

    }

    @Test
    public void QInterfaceType_relation() throws SecurityException, NoSuchFieldException {
        start(QInterfaceTypeTest_InterfaceType.class, QInterfaceTypeTest_InterfaceType.interfaceType);
        match(QInterfaceTypeTest_InterfaceType.class, "relation");
    }

    @Test
    public void QInterfaceType_relation2() throws SecurityException, NoSuchFieldException {
        start(QInterfaceTypeTest_InterfaceType.class, QInterfaceTypeTest_InterfaceType.interfaceType);
        match(ListPath.class, "relation2");
    }

    @Test
    public void QInterfaceType_relation3() throws SecurityException, NoSuchFieldException {
        start(QInterfaceTypeTest_InterfaceType.class, QInterfaceTypeTest_InterfaceType.interfaceType);
        match(ListPath.class, "relation3");
    }

    @Test
    public void QInterfaceType_relation4() throws SecurityException, NoSuchFieldException {
        start(QInterfaceTypeTest_InterfaceType.class, QInterfaceTypeTest_InterfaceType.interfaceType);
        match(NumberPath.class, "relation4");
    }

    @Test
    public void QInterfaceType3() throws SecurityException, NoSuchFieldException {
        Class<?> cl = QInterfaceTypeTest_InterfaceType3.class;
        cl.getField("prop");
        cl.getField("prop2");
        cl.getField("prop3");
    }

    @Test
    public void QInterfaceType5() throws SecurityException, NoSuchFieldException{
        Class<?> cl = QInterfaceTypeTest_InterfaceType5.class;
        cl.getField("prop");
        cl.getField("prop2");
        cl.getField("prop3");
        cl.getField("prop4");
        cl.getField("prop5");
    }

}
