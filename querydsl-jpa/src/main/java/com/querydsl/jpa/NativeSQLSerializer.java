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
package com.querydsl.jpa;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.*;
import com.querydsl.core.types.*;

/**
 * NativeSQLSerializer extends the SQLSerializer class to extract referenced entity paths and change
 * some serialization formats
 *
 * @author tiwe
 *
 */
public final class NativeSQLSerializer extends SQLSerializer {

    private final ListMultimap<Expression<?>, String> aliases = ArrayListMultimap.create();

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
            SQLTemplates templates = getTemplates();
            Column column = path.getAnnotatedElement().getAnnotation(Column.class);
            append(templates.quoteIdentifier(column.name(), precededByDot));
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
        return expr instanceof Operation && ((Operation<?>)expr).getOperator() == Ops.ALIAS;
    }

    public ListMultimap<Expression<?>, String> getAliases() {
        return aliases;
    }

    private boolean isAllExpression(Expression<?> expr) {
        if (expr instanceof Operation) {
            return ((Operation<?>)expr).getOperator() == SQLOps.ALL;
        } else if (expr instanceof TemplateExpression) {
            return ((TemplateExpression<?>)expr).getTemplate().toString().equals("*");
        } else {
            return false;
        }
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow) {
        // TODO get rid of this wrapping when Hibernate doesn't require unique aliases anymore
        int size = metadata.getProjection().size();
        Expression<?>[] args = metadata.getProjection().toArray(new Expression<?>[size]);
        boolean modified = false;
        Set<String> used = new HashSet<String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Path) {
                Path<?> path = (Path<?>)args[i];
                if (!used.add(path.getMetadata().getName())) {
                    String alias = "col__"+(i+1);
                    aliases.put(args[i], alias);
                    args[i] = ExpressionUtils.as(args[i], alias);
                    modified = true;
                } else if (path.getAnnotatedElement().isAnnotationPresent(Column.class)) {
                    aliases.put(path, path.getAnnotatedElement().getAnnotation(Column.class).name());
                } else {
                    aliases.put(path, ColumnMetadata.getName(path));
                }
            } else if (args[i] instanceof FactoryExpression) {
                FactoryExpression<?> factoryExpr = (FactoryExpression<?>)args[i];
                List<Expression<?>> fargs = Lists.newArrayList(factoryExpr.getArgs());
                for (int j = 0; j < fargs.size(); j++) {
                    if (fargs.get(j) instanceof Path) {
                        Path<?> path = (Path<?>) fargs.get(j);
                        String columnName;
                        if (path.getAnnotatedElement().isAnnotationPresent(Column.class)) {
                            columnName = path.getAnnotatedElement().getAnnotation(Column.class).name();
                        } else {
                            columnName = ColumnMetadata.getName(path);
                        }
                        if (!used.add(columnName)) {
                            String alias = "col__"+(i+1)+"_"+(j+1);
                            aliases.put(path, alias);
                            fargs.set(j, ExpressionUtils.as(fargs.get(j), alias));
                        } else {
                            aliases.put(path, columnName);
                        }
                    } else if (isAlias(fargs.get(j))) {
                        Operation<?> operation = (Operation<?>)fargs.get(j);
                        aliases.put(operation, operation.getArg(1).toString());
                    } else if (!isAllExpression(fargs.get(j))) {
                        String alias = "col__"+(i+1)+"_"+(j+1);
                        aliases.put(fargs.get(j), alias);
                        fargs.set(j, ExpressionUtils.as(fargs.get(j), alias));
                    }
                }
                args[i] = new QTuple(ImmutableList.copyOf(fargs));
                modified = true;
            } else if (isAlias(args[i])) {
                Operation<?> operation = (Operation<?>)args[i];
                aliases.put(operation, operation.getArg(1).toString());
            } else {
                // https://github.com/mysema/querydsl/issues/80
                if (!isAllExpression(args[i])) {
                    String alias = "col__"+(i+1);
                    aliases.put(args[i], alias);
                    args[i] = ExpressionUtils.as(args[i], alias);
                    modified = true;
                }
            }
        }
        if (modified) {
            metadata = metadata.clone();
            metadata.clearProjection();
            for (Expression<?> arg : args) {
                metadata.addProjection(arg);
            }
        }
        super.serialize(metadata, forCountRow);
    }

    @Override
    public void visitConstant(Object constant) {
        if (constant instanceof Collection<?>) {
            append("(");
            boolean first = true;
            for (Object element : ((Collection<?>)constant)) {
                if (!first) {
                    append(", ");
                }
                visitConstant(element);
                first = false;
            }
            append(")");
        } else if (!getConstantToLabel().containsKey(constant)) {
            String constLabel = String.valueOf(getConstantToLabel().size() + 1);
            getConstantToLabel().put(constant, constLabel);
            append("?"+constLabel);
        } else {
            append("?"+getConstantToLabel().get(constant));
        }
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<? extends Expression<?>> args) {
        if (operator == SQLOps.ALL
                && !RelationalPath.class.isInstance(args.get(0))
                && wrapEntityProjections) {
            append("{");
            super.visitOperation(type, operator, args);
            append("}");
        } else {
            super.visitOperation(type, operator, args);
        }
    }

}
