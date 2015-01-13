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
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QReservedNamesTest_ReservedNames;

public class ReservedNamesTest {

    @QueryEntity
    public static class ReservedNames {

        public boolean isNew() {
            return false;
        }

        public String getPackage() {
            return "";
        }

        public int getProtected() {
            return 1;
        }

        public List<ReservedNames> getIf() {
            return null;
        }

        public Set<ReservedNames> getElse() {
            return null;
        }

        public List<String> getTry() {
            return null;
        }

        public Set<Integer> getCatch() {
            return null;
        }

        public Map<String, ReservedNames> getWhile() {
            return null;
        }

        public Map<String, String> getFor() {
            return null;
        }

        public ReservedNames getExtends() {
            return null;
        }

    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException {
        Class<?> cl = QReservedNamesTest_ReservedNames.class;
        cl.getField("new$");
        cl.getField("package$");
        cl.getField("protected$");
        cl.getField("if$");
        cl.getField("else$");
        cl.getField("try$");
        cl.getField("catch$");
        cl.getField("while$");
        cl.getField("for$");
        cl.getField("extends$");
    }

}
