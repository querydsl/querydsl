package com.querydsl.r2dbc;

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindMarkers;

import java.util.List;

/**
 * R2dbcUtils
 */
public final class R2dbcUtils {

    private R2dbcUtils() {
    }

    public static String replaceBindingArguments(BindMarkers bindMarkers, List<Object> objects, String originalSql) {
        String sql = originalSql;
        for (Object o : objects) {
            int index = sql.indexOf('?');
            if (index == -1) {
                break;
            }
            BindMarker next = bindMarkers.next();
            String first = sql.substring(0, index);
            String second = sql.substring(index + 1);
            sql = first + next.getPlaceholder() + second;
        }
        return sql;
    }

}