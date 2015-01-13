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
package com.querydsl.sql.types;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * URLType maps URL to URL on the JDBC level
 *
 * @author tiwe
 *
 */
public class URLType extends AbstractType<URL> {

    public URLType() {
        super(Types.VARCHAR);
    }

    public URLType(int type) {
        super(type);
    }

    @Override
    public URL getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getURL(startIndex);
    }

    @Override
    public Class<URL> getReturnedClass() {
        return URL.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, URL value)
            throws SQLException {
        st.setURL(startIndex, value);
    }

}
