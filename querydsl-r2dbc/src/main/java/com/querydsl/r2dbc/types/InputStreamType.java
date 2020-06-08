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
package com.querydsl.r2dbc.types;

import java.io.InputStream;
import java.sql.Types;

/**
 * {@code InputStreamType} maps InputStream to InputStream on the JDBC level
 *
 * @author tiwe
 */
public class InputStreamType extends AbstractType<InputStream> {

    public InputStreamType() {
        super(Types.BLOB);
    }

    public InputStreamType(int type) {
        super(type);
    }

    @Override
    public Class<InputStream> getReturnedClass() {
        return InputStream.class;
    }

}