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
package com.querydsl.sql.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.codegen.utils.model.Types;
import com.querydsl.codegen.EntityType;

public class OriginalNamingStrategyTest {

    private NamingStrategy namingStrategy = new OriginalNamingStrategy();

    private EntityType entityModel;

    @Before
    public void setUp() {
        entityModel = new EntityType(Types.OBJECT);
        //entityModel.addAnnotation(new TableImpl("OBJECT"));
        entityModel.getData().put("table", "OBJECT");
    }

    @Test
    public void getClassName() {
        assertEquals("user_data", namingStrategy.getClassName("user_data"));
        assertEquals("u", namingStrategy.getClassName("u"));
        assertEquals("us",namingStrategy.getClassName("us"));
        assertEquals("u_", namingStrategy.getClassName("u_"));
        assertEquals("us_",namingStrategy.getClassName("us_"));

        assertEquals("new_line", namingStrategy.getClassName("new line"));
    }

    @Test
    public void getPropertyName() {
        assertEquals("while_col", namingStrategy.getPropertyName("while", entityModel));
        assertEquals("name", namingStrategy.getPropertyName("name", entityModel));
        assertEquals("user_id", namingStrategy.getPropertyName("user_id", entityModel));
        assertEquals("accountEvent_id", namingStrategy.getPropertyName("accountEvent_id", entityModel));

        assertEquals("_123abc", namingStrategy.getPropertyName("123abc", entityModel));
        assertEquals("_123_abc", namingStrategy.getPropertyName("123 abc", entityModel));

        assertEquals("_123_abc_def", namingStrategy.getPropertyName("#123#abc#def", entityModel));

        assertEquals("new_line", namingStrategy.getPropertyName("new line", entityModel));
    }

    @Test
    public void getPropertyName_with_dashes() {
        assertEquals("A_FOOBAR", namingStrategy.getPropertyName("A-FOOBAR" , entityModel));
        assertEquals("A_FOOBAR", namingStrategy.getPropertyName("A_FOOBAR" , entityModel));
    }

    @Test
    public void getPropertyNameForInverseForeignKey() {
        assertEquals("_fk_superior", namingStrategy.getPropertyNameForInverseForeignKey("fk_superior", entityModel));
    }

    @Test
    public void getPropertyNameForForeignKey() {
        assertEquals("fk_superior", namingStrategy.getPropertyNameForForeignKey("fk_superior", entityModel));
        assertEquals("FK_SUPERIOR", namingStrategy.getPropertyNameForForeignKey("FK_SUPERIOR", entityModel));
    }

    @Test
    public void getDefaultVariableName() {
        assertEquals("OBJECT", namingStrategy.getDefaultVariableName(entityModel));
    }
}
