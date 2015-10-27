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
package com.querydsl.sql.codegen.support;

import com.querydsl.sql.Configuration;

/**
 * {@code NumericMapping} customizes mappings of various numeric precisions to data types.
 *
 * @author tiwe
 *
 */
public class NumericMapping implements Mapping {

    private int total, decimal;

    private String javaType;

    @Override
    public void apply(Configuration configuration) {
        try {
            configuration.registerNumeric(total, decimal, Class.forName(javaType));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}
