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

import java.sql.Statement;

import com.querydsl.core.annotations.Immutable;

/**
 * {@code StatementOptions} holds parameters that should be applied to {@link Statement}s.
 */
@Immutable
public class StatementOptions {

    public static final StatementOptions DEFAULT = new StatementOptions(null, null, null, null);

    private final Integer maxFieldSize;
    private final Integer maxRows;
    private final Integer queryTimeout;
    private final Integer fetchSize;

    public StatementOptions(Integer maxFieldSize, Integer maxRows, Integer queryTimeout, Integer fetchSize) {
        this.maxFieldSize = maxFieldSize;
        this.maxRows = maxRows;
        this.queryTimeout = queryTimeout;
        this.fetchSize = fetchSize;
    }

    public Integer getMaxFieldSize() {
        return maxFieldSize;
    }

    public Integer getMaxRows() {
        return maxRows;
    }

    public Integer getQueryTimeout() {
        return queryTimeout;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link StatementOptions}
     */
    public static final class Builder {
        private Integer maxFieldSize;
        private Integer maxRows;
        private Integer queryTimeout;
        private Integer fetchSize;

        private Builder() { }

        public Builder setMaxFieldSize(Integer maxFieldSize) {
            this.maxFieldSize = maxFieldSize;
            return this;
        }

        public Builder setMaxRows(Integer maxRows) {
            this.maxRows = maxRows;
            return this;
        }

        public Builder setQueryTimeout(Integer queryTimeout) {
            this.queryTimeout = queryTimeout;
            return this;
        }

        public Builder setFetchSize(Integer fetchSize) {
            this.fetchSize = fetchSize;
            return this;
        }

        public StatementOptions build() {
            return new StatementOptions(maxFieldSize, maxRows, queryTimeout, fetchSize);
        }
    }
}
