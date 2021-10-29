/*
 * Copyright 2021, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.group;

import com.querydsl.core.types.Expression;

import java.util.ArrayList;
import java.util.List;

class GConcat<T> extends AbstractGroupExpression<T, String> {

    private final String delimiter;

    GConcat(Expression<T> expr, String delimiter) {
        super(String.class, expr);
        this.delimiter = delimiter;
    }

    @Override
    public GroupCollector<T, String> createGroupCollector() {
        return new GroupCollector<T, String>() {

            private final List<String> list = new ArrayList<>();

            @Override
            public void add(T o) {
                if (o != null) {
                    list.add(o.toString());
                }
            }

            @Override
            public String get() {
                return String.join(delimiter, list);
            }

        };
    }

}
