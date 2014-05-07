/*
 * Copyright 2013, Mysema Ltd
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
package com.mysema.query.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Normalization {

    private static final String WS = "\\s*";

    private static final String START = "\\b";

    private static final String NUMBER = "([+\\-]?\\d+\\.?\\d*)";

    private static final Pattern FULL_OPERATION = Pattern.compile(
            "(\\A|[^\\d\\*/\\+\\-\"' ])" + WS +
            "(" + NUMBER + WS + "[+\\-/*]" + WS + ")+" + NUMBER + WS +
            "(\\Z|[^\\d\\*/\\+\\-\"' ])");

    private static final Pattern[] OPERATIONS = {
            Pattern.compile(START + NUMBER + WS + "\\*" + WS + NUMBER),
            Pattern.compile(START + NUMBER + WS + "/" + WS + NUMBER),
            Pattern.compile(START + NUMBER + WS + "\\+" + WS + NUMBER),
            Pattern.compile(START + NUMBER + WS + "\\-" + WS + NUMBER)
    };

    private static String normalizeOperation(String queryString) {
        for (int i = 0; i < OPERATIONS.length; i++) {
            Pattern operation = OPERATIONS[i];
            Matcher matcher;
            while ((matcher = operation.matcher(queryString)).find()) {
                BigDecimal first = new BigDecimal(matcher.group(1));
                BigDecimal second = new BigDecimal(matcher.group(2));
                BigDecimal result;
                switch (i) {
                    case 0: result = first.multiply(second); break;
                    case 1: result = first.divide(second, 10, RoundingMode.HALF_UP); break;
                    case 2: result = first.add(second); break;
                    case 3: result = first.subtract(second); break;
                    default: throw new IllegalStateException();
                }
                StringBuffer buffer = new StringBuffer();
                matcher.appendReplacement(buffer, result.stripTrailingZeros().toPlainString())
                        .appendTail(buffer);
                queryString = buffer.toString();
            }
        }
        return queryString;
    }

    public static String normalize(String queryString) {
        if (!hasOperators(queryString)) {
            return queryString;
        }

        StringBuilder rv = null;
        Matcher m = FULL_OPERATION.matcher(queryString);
        int end = 0;
        while (m.find()) {
            if (rv == null) {
                rv = new StringBuilder(queryString.length());
            }
            if (m.start() > end) {
                rv.append(queryString.subSequence(end, m.start()));
            }
            String result = normalizeOperation(queryString.substring(m.start(), m.end()));
            rv.append(result);
            end = m.end();
        }
        if (end > 0) {
            if (end < queryString.length()) {
                rv.append(queryString.substring(end));
            }
            if (rv.toString().equals(queryString)) {
                return rv.toString();
            } else {
                return normalize(rv.toString());
            }
        } else {
            return queryString;
        }
    }

    private static boolean hasOperators(String queryString) {
        for (int i = 0; i < queryString.length(); i++) {
            char ch = queryString.charAt(i);
            if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                return true;
            }
        }
        return false;
    }

    private Normalization() {}

}
