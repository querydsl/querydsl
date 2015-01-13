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
package com.querydsl.sql;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.querydsl.core.alias.Gender;
import com.querydsl.sql.domain.QSurvey;
import com.querydsl.sql.types.*;
import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConfigurationTest {

    @Test
    public void Various() {
        Configuration configuration = new Configuration(new H2Templates());
//        configuration.setJavaType(Types.DATE, java.util.Date.class);
        configuration.register(new UtilDateType());
        configuration.register("person", "secureId", new EncryptedString());
        configuration.register("person", "gender",  new EnumByNameType<Gender>(Gender.class));
        configuration.register(new StringType());
        assertEquals(Gender.class, configuration.getJavaType(java.sql.Types.VARCHAR, null, 0,0,"person", "gender"));
    }

    @Test
    public void Custom_Type() {
        Configuration configuration = new Configuration(new H2Templates());
//        configuration.setJavaType(Types.BLOB, InputStream.class);
        configuration.register(new InputStreamType());
        assertEquals(InputStream.class, configuration.getJavaType(Types.BLOB, null, 0,0,"", ""));
    }

    @Test
    public void Set_Null() throws SQLException {
        Configuration configuration = new Configuration(new H2Templates());
//        configuration.register(new UntypedNullType());
        configuration.register("SURVEY", "NAME",  new EncryptedString());
        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        configuration.set(stmt, QSurvey.survey.name, 0, Null.DEFAULT);
    }

    @Test
    public void Get_Schema() {
        Configuration configuration = new Configuration(new H2Templates());
        configuration.registerSchemaOverride("public", "pub");
        configuration.registerTableOverride("employee", "emp");
        configuration.registerTableOverride("public", "employee", "employees");

        assertEquals("pub", configuration.getOverride(new SchemaAndTable("public", "")).getSchema());
        assertEquals("emp", configuration.getOverride(new SchemaAndTable("", "employee")).getTable());
        assertEquals("employees", configuration.getOverride(new SchemaAndTable("public", "employee")).getTable());
//        assertEquals("pub", configuration.getSchema("public"));
//        assertEquals("emp", configuration.getTable("", "employee"));
//        assertEquals("employees", configuration.getTable("public", "employee"));
    }

    @Test
    public void NumericOverriden() {
        Configuration configuration = new Configuration(new H2Templates());
        configuration.registerNumeric(19, 0, BigInteger.class);
        assertEquals(configuration.getJavaType(Types.NUMERIC, "", 19, 0, "", ""), BigInteger.class);
    }

    @Test
    public void NumericOverriden2() {
        Configuration configuration = new Configuration(new H2Templates());
        configuration.registerNumeric(18, 19, 0, 0, BigInteger.class);
        assertEquals(configuration.getJavaType(Types.NUMERIC, "", 18, 0, "", ""), BigInteger.class);
        assertEquals(configuration.getJavaType(Types.NUMERIC, "", 19, 0, "", ""), BigInteger.class);
    }


}
