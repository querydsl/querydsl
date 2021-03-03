/*
 * Copyright 2010, Mysema Ltd
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
package com.querydsl.codegen.utils;

import java.util.Arrays;

public final class StringUtils {
    
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public static String uncapitalize(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String escapeJava(String str) {        
        str = str.replace("\\", "\\\\");
        str = str.replace("\"", "\\\"");
        str = str.replace("\r", "\\\r");
        str = str.replace("\t", "\\\t");
        str = str.replace("\n", "\\\n");
        return str;
    }

    public static String repeat(char value, int times) {
        char[] chars = new char[times];
        Arrays.fill(chars, value);
        return new String(chars);
    }
    
    private StringUtils() {}

}
