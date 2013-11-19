package com.mysema.query.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Normalization {

    private static final String WS = "\\s*";

    private static final String START = "\\b";

    private static final String NUMBER = "([+\\-]?\\d+\\.?\\d*)";

    private static final Pattern OPERATOR = Pattern.compile(WS + "[+\\-/*]" + WS);

    private static final Pattern OPERATION = Pattern.compile(START + NUMBER + OPERATOR.pattern() + NUMBER);

    private static final Pattern ADDITION = Pattern.compile(START + NUMBER + WS + "\\+" + WS + NUMBER);

    private static final Pattern SUBTRACTION = Pattern.compile(START + NUMBER + WS + "\\-" + WS + NUMBER);

    private static final Pattern DIVISION = Pattern.compile(START + NUMBER + WS + "/" + WS + NUMBER);

    private static final Pattern MULTIPLICATION = Pattern.compile(START + NUMBER + WS + "\\*" + WS + NUMBER);

    public static final String normalize(String queryString) {
        if (!hasOperators(queryString)) {
            return queryString;
        }

        StringBuilder rv = null;
        Matcher m = OPERATION.matcher(queryString);
        int end = 0;
        while (m.find()) {
            if (rv == null) {
                rv = new StringBuilder(queryString.length());
            }
            if (m.start() > 0 && queryString.charAt(m.start() - 1) == '\'') {
                continue;
            } else if (m.end() < queryString.length() && queryString.charAt(m.end()) == '\'') {
                continue;
            }
            if (m.start() > end) {
                rv.append(queryString.subSequence(end, m.start()));
            }
            String str = queryString.substring(m.start(), m.end());
            boolean add = ADDITION.matcher(str).matches();
            boolean subtract = SUBTRACTION.matcher(str).matches();
            boolean divide = DIVISION.matcher(str).matches();
            boolean multiply = MULTIPLICATION.matcher(str).matches();
            Matcher matcher = OPERATION.matcher(str);
            matcher.matches();
            BigDecimal first = new BigDecimal(matcher.group(1));
            BigDecimal second = new BigDecimal(matcher.group(2));
            String result = null;
            if (multiply) {
                result = first.multiply(second).toString();
            } else if (divide) {
                result = first.divide(second, 10, RoundingMode.HALF_UP).toString();
            } else if (subtract) {
                result = first.subtract(second).toString();
            } else if (add) {
                result = first.add(second).toString();
            } else {
                throw new IllegalStateException("Unsupported expression " + str);
            }
            while (result.contains(".") && (result.endsWith("0") || result.endsWith("."))) {
                result = result.substring(0, result.length()-1);
            }
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

    private static final boolean hasOperators(String queryString) {
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
