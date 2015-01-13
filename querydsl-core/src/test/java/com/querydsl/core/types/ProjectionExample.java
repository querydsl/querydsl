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

public class ProjectionExample {

    public Long id;

    public String text;

    public ProjectionExample() {

    }

    public ProjectionExample(Long id) {
        this.id = id;
    }

    public ProjectionExample(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public ProjectionExample(CharSequence text) {
        this.text = text.toString();
    }

    public ProjectionExample(boolean booleanArg, byte byteArg,
            char charArg, short shortArg,
            int intArg, long longArg,
            float floatArg, double doubleArg) {

    }

    public ProjectionExample(long id, char... characters) {
        this.id = id;
        this.text = String.valueOf(characters);
    }

}
