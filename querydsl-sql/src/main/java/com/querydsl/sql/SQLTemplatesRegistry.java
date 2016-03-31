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
package com.querydsl.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * {@code SQLTemplatesRegistry} is a registry for SQLTemplates instances
 */
public class SQLTemplatesRegistry {

    /**
     * Get the SQLTemplates instance that matches best the SQL engine of the
     * given database metadata
     *
     * @param md database metadata
     * @return templates
     * @throws SQLException
     */
    public SQLTemplates getTemplates(DatabaseMetaData md) throws SQLException {
        return getBuilder(md).build();
    }

    /**
     * Get a SQLTemplates.Builder instance that matches best the SQL engine of the
     * given database metadata
     *
     * @param md database metadata
     * @return templates
     * @throws SQLException
     */
    public SQLTemplates.Builder getBuilder(DatabaseMetaData md) throws SQLException {
        String name = md.getDatabaseProductName().toLowerCase();
        if (name.equals("cubrid")) {
            return CUBRIDTemplates.builder();
        } else if (name.equals("apache derby")) {
            return DerbyTemplates.builder();
        } else if (name.startsWith("firebird")) {
            return FirebirdTemplates.builder();
        } else if (name.equals("h2")) {
            return H2Templates.builder();
        } else if (name.equals("hsql")) {
            return HSQLDBTemplates.builder();
        } else if (name.equals("mysql")) {
            return MySQLTemplates.builder();
        } else if (name.equals("oracle")) {
            return OracleTemplates.builder();
        } else if (name.equals("postgresql")) {
            return PostgreSQLTemplates.builder();
        } else if (name.equals("sqlite")) {
            return SQLiteTemplates.builder();
        } else if (name.startsWith("teradata")) {
            return TeradataTemplates.builder();
        } else if (name.equals("microsoft sql server")) {
            switch (md.getDatabaseMajorVersion()) {
                case 12:
                case 11: return SQLServer2012Templates.builder();
                case 10: return SQLServer2008Templates.builder();
                case 9:  return SQLServer2005Templates.builder();
                default: return SQLServerTemplates.builder();
            }
        } else {
            return new SQLTemplates.Builder() {
                @Override
                protected SQLTemplates build(char escape, boolean quote) {
                    return new SQLTemplates(Keywords.DEFAULT, "\"", escape, quote);
                }
            };
        }
    }

}
