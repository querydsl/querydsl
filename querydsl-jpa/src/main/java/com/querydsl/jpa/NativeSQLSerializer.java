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
package com.querydsl.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Table;

import com.querydsl.core.JoinExpression;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.*;
import com.querydsl.sql.*;

/**
 * {@code NativeSQLSerializer} extends {@link SQLSerializer} to extract referenced entity paths and change
 * some serialization formats
 *
 * @author tiwe
 *
 */
public final class NativeSQLSerializer extends SQLSerializer {

    private final Map<Expression<?>, List<String>> aliases = new HashMap<>();

    private final boolean wrapEntityProjections;

    public NativeSQLSerializer(Configuration configuration) {
        this(configuration, false);
    }

    public NativeSQLSerializer(Configuration configuration, boolean wrapEntityProjections) {
        super(configuration);
        this.wrapEntityProjections = wrapEntityProjections;
    }

    @Override
    protected void appendAsColumnName(Path<?> path, boolean precededByDot) {
        if (path.getAnnotatedElement().isAnnotationPresent(Column.class)) {
            Column column = path.getAnnotatedElement().getAnnotation(Column.class);
            if (!column.name().isEmpty()) {
                append(getTemplates().quoteIdentifier(column.name(), precededByDot));
            } else {
                super.appendAsColumnName(path, precededByDot);
            }
        } else {
            super.appendAsColumnName(path, precededByDot);
        }
    }

    @Override
    protected void handleJoinTarget(JoinExpression je) {
        SQLTemplates templates = getTemplates();
        Class<?> type = je.getTarget().getType();
        if (type.isAnnotationPresent(Table.class) && templates.isSupportsAlias()) {
            Table table = type.getAnnotation(Table.class);
            boolean precededByDot;
            if (!table.schema().isEmpty() && templates.isPrintSchema()) {
                appendSchemaName(table.schema());
                append(".");
                precededByDot = true;
            } else {
                precededByDot = false;
            }
            appendTableName(table.name(), precededByDot);
            append(templates.getTableAlias());
        }
        super.handleJoinTarget(je);
    }

    private boolean isAlias(Expression<?> expr) {
        return expr instanceof Operation && ((Operation<?>) expr).getOperator() == Ops.ALIAS;
    }

    public Map<Expression<?>, List<String>> getAliases() {
        return aliases;
    }

    private boolean isAllExpression(Expression<?> expr) {
        if (expr instanceof Operation) {
            return ((Operation<?>) expr).getOperator() == SQLOps.ALL;
        } else if (expr instanceof TemplateExpression) {
            return ((TemplateExpression<?>) expr).getTemplate().toString().equals("*");
        } else {
            return false;
        }
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow) {
        // TODO get rid of this wrapping when Hibernate doesn't require unique aliases anymore
        boolean modified = false;
        Set<String> used = new HashSet<String>();
        Expression<?> projection = metadata.getProjection();
        if (projection instanceof Path) {
            Path<?> path = (Path<?>) projection;
            if (!used.add(path.getMetadata().getName())) {
                String alias = "col_1";
                aliases.computeIfAbsent(projection, NativeSQLSerializer::createArrayList).add(alias);
                projection = ExpressionUtils.as(projection, alias);
                modified = true;
            } else if (path.getAnnotatedElement().isAnnotationPresent(Column.class)) {
                Column column = path.getAnnotatedElement().getAnnotation(Column.class);
                if (!column.name().isEmpty()) {
                    aliases.computeIfAbsent(projection, NativeSQLSerializer::createArrayList).add(column.name());
                } else {
                    aliases.computeIfAbsent(projection, NativeSQLSerializer::createArrayList).add(ColumnMetadata.getName(path));
                }
            } else {
                aliases.computeIfAbsent(projection, NativeSQLSerializer::createArrayList).add(ColumnMetadata.getName(path));
            }
        } else if (projection instanceof FactoryExpression) {
            FactoryExpression<?> factoryExpr = (FactoryExpression<?>) projection;
            List<Expression<?>> fargs = new ArrayList<>(factoryExpr.getArgs());
            for (int j = 0; j < fargs.size(); j++) {
                if (fargs.get(j) instanceof Path) {
                    Path<?> path = (Path<?>) fargs.get(j);
                    String columnName;
                    if (path.getAnnotatedElement().isAnnotationPresent(Column.class)) {
                        Column column = path.getAnnotatedElement().getAnnotation(Column.class);
                        if (!column.name().isEmpty()) {
                            columnName = column.name();
                        } else {
                            columnName = ColumnMetadata.getName(path);
                        }
                    } else {
                        columnName = ColumnMetadata.getName(path);
                    }
                    if (!used.add(columnName)) {
                        String alias = "col_" + (j + 1);
                        aliases.computeIfAbsent(path, NativeSQLSerializer::createArrayList).add(alias);
                        fargs.set(j, ExpressionUtils.as(fargs.get(j), alias));
                    } else {
                        aliases.computeIfAbsent(path, NativeSQLSerializer::createArrayList).add(columnName);
                    }
                } else if (isAlias(fargs.get(j))) {
                    Operation<?> operation = (Operation<?>) fargs.get(j);
                    aliases.computeIfAbsent(operation, NativeSQLSerializer::createArrayList).add(operation.getArg(1).toString());
                } else if (!isAllExpression(fargs.get(j))) {
                    String alias = "col_" + (j + 1);
                    aliases.computeIfAbsent(fargs.get(j), NativeSQLSerializer::createArrayList).add(alias);
                    fargs.set(j, ExpressionUtils.as(fargs.get(j), alias));
                }
            }
            projection = Projections.tuple(new ArrayList<>(fargs));
            modified = true;
        } else if (isAlias(projection)) {
            Operation<?> operation = (Operation<?>) projection;
            aliases.computeIfAbsent(operation, NativeSQLSerializer::createArrayList).add(operation.getArg(1).toString());
        } else {
            // https://github.com/querydsl/querydsl/issues/80
            if (!isAllExpression(projection)) {
                String alias = "col_1";
                aliases.computeIfAbsent(projection, NativeSQLSerializer::createArrayList).add(alias);
                projection = ExpressionUtils.as(projection, alias);
                modified = true;
            }
        }
        if (modified) {
            metadata = metadata.clone();
            metadata.setProjection(projection);
        }
        super.serialize(metadata, forCountRow);
    }

    private static <T> ArrayList<T> createArrayList(Object key) {
        return new ArrayList<T>();
    }

    @Override
    public void visitConstant(Object constant) {
        if (constant instanceof Collection<?>) {
            append("(");
            boolean first = true;
            for (Object element : ((Collection<?>) constant)) {
                if (!first) {
                    append(", ");
                }
                visitConstant(element);
                first = false;
            }
            append(")");
        } else {
            super.visitConstant(constant);
        }
    }

    @Override
    protected void serializeConstant(int parameterIndex, String constantLabel) {
        append("?");
        append(Integer.toString(parameterIndex));
    }

    @Override
    protected void visitOperation(Class<?> type, Operator operator, List<? extends Expression<?>> args) {
        if (operator == SQLOps.ALL
                && !(args.get(0) instanceof RelationalPath)
                && wrapEntityProjections) {
            append("{");
            super.visitOperation(type, operator, args);
            append("}");
        } else {
            super.visitOperation(type, operator, args);
        }
    }

}
