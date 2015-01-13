package com.querydsl.jpa;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.path.PathBuilder;

public class OrderHelper {

    private static final Pattern DOT = Pattern.compile("\\.");

    public static PathBuilder<?> join(JPACommonQuery<?> query, PathBuilder<?> builder, Map<String, PathBuilder<?>> joins, String path) {
        PathBuilder<?> rv = joins.get(path);
        if (rv == null) {
            if (path.contains(".")) {
                String[] tokens = DOT.split(path);
                String[] parent = new String[tokens.length - 1];
                System.arraycopy(tokens, 0, parent, 0, tokens.length - 1);
                String parentKey = StringUtils.join(parent, ".");
                builder = join(query, builder, joins, parentKey);
                rv = new PathBuilder(Object.class, StringUtils.join(tokens, "_"));
                query.leftJoin((EntityPath)builder.get(tokens[tokens.length - 1]), rv);
            } else {
                rv = new PathBuilder(Object.class, path);
                query.leftJoin((EntityPath)builder.get(path), rv);
            }
            joins.put(path, rv);
        }
        return rv;
    }

    public static void orderBy(JPACommonQuery<?> query, EntityPath<?> entity, List<String> order) {
        PathBuilder<?> builder = new PathBuilder(entity.getType(), entity.getMetadata());
        Map<String, PathBuilder<?>> joins = Maps.newHashMap();

        for (String entry : order) {
            String[] tokens = DOT.split(entry);
            if (tokens.length > 1) {
                String[] parent = new String[tokens.length - 1];
                System.arraycopy(tokens, 0, parent, 0, tokens.length - 1);
                PathBuilder<?> parentAlias = join(query, builder, joins, StringUtils.join(parent, "."));
                query.orderBy(parentAlias.getString(tokens[tokens.length - 1]).asc());
            } else {
                query.orderBy(builder.getString(tokens[0]).asc());
            }
        }
    }

}


