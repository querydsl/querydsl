/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.sql.codegen;

import static com.querydsl.core.util.JavaSyntaxUtils.isReserved;

/**
 * @author tiwe
 *
 */
public final class Naming {
    
    public static String normalize(String s, String reservedSuffix) {
        if (isReserved(s)) {
            return s + reservedSuffix;
        } else {
            StringBuilder sb = new StringBuilder(s.length() + 1);
            for (char c : s.toCharArray()) {
                if(!Character.isJavaIdentifierPart(c)) {
                    sb.append("_");
                } else if (sb.length() == 0 && !Character.isJavaIdentifierStart(c)) {
                    sb.append("_").append(c);
                } else {                    
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }
    
    private Naming() {}

}
