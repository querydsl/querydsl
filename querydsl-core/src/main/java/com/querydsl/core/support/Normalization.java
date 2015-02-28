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
package com.querydsl.core.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Normalization {

    private static final String WS = "\\s*";

    private static final String NUMBER = "([\\+\\-]?\\d+\\.?\\d*)";

    // TODO simplify
    private static final Pattern FULL_OPERATION = Pattern.compile(
            "(?<![\\d\\*/\"' ])" + "(\\b|\\(|\\s+)"  +
            "(" + NUMBER + WS + "[+\\-/*]" + WS + ")+" + NUMBER + WS +
            "(?![\\d\\*/\"' ])");

    private static final Pattern[] OPERATIONS = {
            Pattern.compile(NUMBER + WS + "(\\*|/)" + WS + NUMBER),
            Pattern.compile(NUMBER + WS + "(\\+|\\-)" + WS + NUMBER),
    };

    private static String normalizeOperation(String queryString) {
        for (int i = 0; i < OPERATIONS.length; i++) {
            Pattern operation = OPERATIONS[i];
            Matcher matcher;
            while ((matcher = operation.matcher(queryString)).find()) {
                char operator = matcher.group(2).charAt(0);
                BigDecimal first = new BigDecimal(matcher.group(1));
                BigDecimal second = new BigDecimal(matcher.group(3));
                BigDecimal result;
                switch (operator) {
                    case '*': result = first.multiply(second); break;
                    case '/': result = first.divide(second, 10, RoundingMode.HALF_UP); break;
                    case '+': result = first.add(second); break;
                    case '-': result = first.subtract(second); break;
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

        StringBuffer buffer = new StringBuffer();
        Matcher m = FULL_OPERATION.matcher(queryString);
        while (m.find()) {
            String result = normalizeOperation(queryString.substring(m.start(), m.end()));
            m.appendReplacement(buffer, result);
        }
        m.appendTail(buffer);
        return buffer.toString();
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
