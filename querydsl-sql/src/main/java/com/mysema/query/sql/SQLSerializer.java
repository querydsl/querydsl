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
package com.mysema.query.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Pair;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.SerializerBase;
import com.mysema.query.types.Constant;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Template;
import com.mysema.query.types.Template.Element;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.TemplateFactory;

/**
 * SqlSerializer serializes Querydsl queries into SQL
 *
 * @author tiwe
 */
public class SQLSerializer extends SerializerBase<SQLSerializer> {

    protected enum Stage {SELECT, FROM, WHERE, GROUP_BY, HAVING, ORDER_BY, MODIFIERS}

    private static final String COMMA = ", ";

    private final List<Path<?>> constantPaths = new ArrayList<Path<?>>();

    private final List<Object> constants = new ArrayList<Object>();

    private final boolean dml;

    protected Stage stage = Stage.SELECT;

    private boolean skipParent;

    private boolean dmlWithSchema;

    private RelationalPath<?> entity;

    private final Configuration configuration;

    private final SQLTemplates templates;

    private boolean inUnion = false;

    private boolean inJoin = false;

    private boolean useLiterals = false;

    public SQLSerializer(Configuration conf) {
        this(conf, false);
    }

    public SQLSerializer(Configuration conf, boolean dml) {
        super(conf.getTemplates());
        this.configuration = conf;
        this.templates = conf.getTemplates();
        this.dml = dml;
    }

    private void appendAsColumnName(Path<?> path) {
        String column = ColumnMetadata.getName(path);
        append(templates.quoteIdentifier(column));
    }

    private Pair<String, String> getSchemaAndTable(RelationalPath<?> path) {
        return configuration.getOverride(Pair.of(path.getSchemaName(), path.getTableName()));
    }

    private void appendSchemaName(String schema) {
        append(templates.quoteIdentifier(schema));
    }

    private void appendTableName(String table) {
        append(templates.quoteIdentifier(table));
    }

    public List<Object> getConstants() {
        return constants;
    }

    public List<Path<?>> getConstantPaths() {
        return constantPaths;
    }

    /**
     * Return a list of expressions that can be used to uniquely define the query sources
     *
     * @param joins
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Expression<?>> getIdentifierColumns(List<JoinExpression> joins, boolean alias) {
        if (joins.size() == 1) {
            JoinExpression join = joins.get(0);
            if (join.getTarget() instanceof RelationalPath) {
                return ((RelationalPath)join.getTarget()).getColumns();
            } else {
                return Collections.emptyList();
            }

        } else {
            List<Expression<?>> rv = Lists.newArrayList();
            int counter = 0;
            for (JoinExpression join : joins) {
                if (join.getTarget() instanceof RelationalPath) {
                    RelationalPath path = (RelationalPath)join.getTarget();
                    List<Expression<?>> columns;
                    if (path.getPrimaryKey() != null) {
                        columns = path.getPrimaryKey().getLocalColumns();
                    } else {
                        columns = path.getColumns();
                    }
                    if (alias) {
                        for (Expression<?> column : columns) {
                            rv.add(ExpressionUtils.as(column, "col" + (++counter)));
                        }
                    } else {
                        rv.addAll(columns);
                    }

                } else {
                    // not able to provide a distinct list of columns
                    return Collections.emptyList();
                }
            }
            return rv;
        }

    }

    protected SQLTemplates getTemplates() {
        return templates;
    }

    public void handle(String template, Object... args) {
        handleTemplate(TemplateFactory.DEFAULT.create(template), Arrays.asList(args));
    }

    private void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof RelationalPath && templates.isSupportsAlias()) {
            final RelationalPath<?> pe = (RelationalPath<?>) je.getTarget();
            if (pe.getMetadata().getParent() == null) {
                Pair<String, String> schemaAndTable = getSchemaAndTable(pe);
                if (templates.isPrintSchema()) {
                    appendSchemaName(schemaAndTable.getFirst());
                    append(".");
                }
                appendTableName(schemaAndTable.getSecond());
                append(templates.getTableAlias());
            }
        }
        inJoin = true;
        handle(je.getTarget());
        inJoin = false;
    }

    public void serialize(QueryMetadata metadata, boolean forCountRow) {
        templates.serialize(metadata, forCountRow, this);
    }

    void serializeForQuery(QueryMetadata metadata, boolean forCountRow) {
        final List<? extends Expression<?>> select = metadata.getProjection();
        final List<JoinExpression> joins = metadata.getJoins();
        final Predicate where = metadata.getWhere();
        final List<? extends Expression<?>> groupBy = metadata.getGroupBy();
        final Predicate having = metadata.getHaving();
        final List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        final Set<QueryFlag> flags = metadata.getFlags();
        final boolean hasFlags = !flags.isEmpty();
        String suffix = null;

        List<Expression<?>> sqlSelect;
        if (select.size() == 1) {
            final Expression<?> first = select.get(0);
            if (first instanceof FactoryExpression) {
                sqlSelect = ((FactoryExpression<?>)first).getArgs();
            } else {
                sqlSelect = (List)select;
            }
        } else {
            sqlSelect = new ArrayList<Expression<?>>(select.size());
            for (Expression<?> selectExpr : select) {
                if (selectExpr instanceof FactoryExpression) {
                    // transforms constructor arguments into individual select expressions
                    sqlSelect.addAll(((FactoryExpression<?>) selectExpr).getArgs());
                } else {
                    sqlSelect.add(selectExpr);
                }
            }
        }

        // with
        if (hasFlags){
            boolean handled = false;
            boolean recursive = false;
            for (QueryFlag flag : flags) {
                if (flag.getPosition() == Position.WITH) {
                    if (flag.getFlag() == SQLTemplates.RECURSIVE) {
                        recursive = true;
                        continue;
                    }
                    if (handled) {
                        append(", ");
                    }
                    handle(flag.getFlag());
                    handled = true;
                }
            }
            if (handled) {
                if (recursive) {
                    prepend(templates.getWithRecursive());
                } else {
                    prepend(templates.getWith());
                }
                append("\n");
            }
        }

        // start
        if (hasFlags) {
            serialize(Position.START, flags);
        }

        // select
        Stage oldStage = stage;
        stage = Stage.SELECT;
        if (forCountRow) {
            append(templates.getSelect());
            if (hasFlags) {
                serialize(Position.AFTER_SELECT, flags);
            }

            if (!metadata.isDistinct()) {
                append(templates.getCountStar());
            } else {
                List<? extends Expression<?>> columns;
                if (sqlSelect.isEmpty()) {
                    columns = getIdentifierColumns(joins, !templates.isCountDistinctMultipleColumns());
                } else {
                    columns = sqlSelect;
                }
                if (columns.size() == 1) {
                    append(templates.getDistinctCountStart());
                    handle(columns.get(0));
                    append(templates.getDistinctCountEnd());
                } else if (templates.isCountDistinctMultipleColumns()) {
                    append(templates.getDistinctCountStart());
                    append("(").handle(COMMA, columns).append(")");
                    append(templates.getDistinctCountEnd());
                } else {
                    // select count(*) from (select distinct ...)
                    append(templates.getCountStar());
                    append(templates.getFrom());
                    append("(");
                    append(templates.getSelectDistinct());
                    handle(COMMA, columns);
                    suffix = ") internal";
                }
            }

        } else if (!sqlSelect.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(templates.getSelect());
            } else {
                append(templates.getSelectDistinct());
            }
            if (hasFlags) {
                serialize(Position.AFTER_SELECT, flags);
            }

            handle(COMMA, sqlSelect);
        }
        if (hasFlags) {
            serialize(Position.AFTER_PROJECTION, flags);
        }

        // from
        stage = Stage.FROM;
        serializeSources(joins);

        // where
        if (where != null) {
            stage = Stage.WHERE;
            if (hasFlags) {
                serialize(Position.BEFORE_FILTERS, flags);
            }
            append(templates.getWhere()).handle(where);
            if (hasFlags) {
                serialize(Position.AFTER_FILTERS, flags);
            }
        }

        // group by
        if (!groupBy.isEmpty()) {
            stage = Stage.GROUP_BY;
            if (hasFlags) {
                serialize(Position.BEFORE_GROUP_BY, flags);
            }
            append(templates.getGroupBy()).handle(COMMA, groupBy);
            if (hasFlags) {
                serialize(Position.AFTER_GROUP_BY, flags);
            }
        }

        // having
        if (having != null) {
            stage = Stage.HAVING;
            if (hasFlags) {
                serialize(Position.BEFORE_HAVING, flags);
            }
            append(templates.getHaving()).handle(having);
            if (hasFlags) {
                serialize(Position.AFTER_HAVING, flags);
            }
        }

        // order by
        if (hasFlags) {
            serialize(Position.BEFORE_ORDER, flags);
        }
        if (!orderBy.isEmpty() && !forCountRow) {
            stage = Stage.ORDER_BY;
            append(templates.getOrderBy());
            handleOrderBy(orderBy);
            if (hasFlags) {
                serialize(Position.AFTER_ORDER, flags);
            }
        }

        // modifiers
        if (!forCountRow && metadata.getModifiers().isRestricting() && !joins.isEmpty()) {
            stage = Stage.MODIFIERS;
            templates.serializeModifiers(metadata, this);
        }

        if (suffix != null) {
            append(suffix);
        }

        // reset stage
        stage = oldStage;

    }

    protected void handleOrderBy(List<OrderSpecifier<?>> orderBy) {
        boolean first = true;
        for (final OrderSpecifier<?> os : orderBy) {
            if (!first) {
                append(COMMA);
            }
            String order = os.getOrder() == Order.ASC ? templates.getAsc() : templates.getDesc();
            if (os.getNullHandling() == OrderSpecifier.NullHandling.NullsFirst) {
                if (templates.getNullsFirst() != null) {
                    handle(os.getTarget());
                    append(order);
                    append(templates.getNullsFirst());
                } else {
                    append("(case when ");
                    handle(os.getTarget());
                    append(" is null then 0 else 1 end), ");
                    handle(os.getTarget());
                    append(order);
                }
            } else if (os.getNullHandling() == OrderSpecifier.NullHandling.NullsLast) {
                if (templates.getNullsLast() != null) {
                    handle(os.getTarget());
                    append(order);
                    append(templates.getNullsLast());
                } else {
                    append("(case when ");
                    handle(os.getTarget());
                    append(" is null then 1 else 0 end), ");
                    handle(os.getTarget());
                    append(order);
                }

            } else {
                handle(os.getTarget());
                append(order);
            }
            first = false;
        }
    }

    public void serializeDelete(QueryMetadata metadata, RelationalPath<?> entity) {
        templates.serializeDelete(metadata, entity, this);
    }

    void serializeForDelete(QueryMetadata metadata, RelationalPath<?> entity) {
        this.entity = entity;
        serialize(Position.START, metadata.getFlags());

        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getDelete());
        }
        serialize(Position.AFTER_SELECT, metadata.getFlags());
        append("from ");

        dmlWithSchema = true;
        handle(entity);
        dmlWithSchema = false;

        if (metadata.getWhere() != null) {
            append(templates.getWhere()).handle(metadata.getWhere());
        }
    }

    public void serializeMerge(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> keys,
            List<Path<?>> columns, List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
        templates.serializeMerge(metadata, entity, keys, columns, values, subQuery, this);
    }

    void serializeForMerge(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> keys,
            List<Path<?>> columns, List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
        this.entity = entity;

        serialize(Position.START, metadata.getFlags());

        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getMergeInto());
        }
        serialize(Position.AFTER_SELECT, metadata.getFlags());

        dmlWithSchema = true;
        handle(entity);
        dmlWithSchema = false;
        append(" ");
        // columns
        if (!columns.isEmpty()) {
            skipParent = true;
            append("(").handle(COMMA, columns).append(") ");
            skipParent = false;
        }
        // keys
        if (!keys.isEmpty()) {
            append(templates.getKey());
            skipParent = true;
            append("(").handle(COMMA, keys).append(") ");
            skipParent = false;
        }

        if (subQuery != null) {
            // subquery
            append("\n");
            serialize(subQuery.getMetadata(), false);
        } else {
            if (!useLiterals) {
                for (int i = 0; i < columns.size(); i++) {
                    if (values.get(i) instanceof Constant<?>) {
                        constantPaths.add(columns.get(i));
                    }
                }
            }

            // values
            append(templates.getValues());
            append("(").handle(COMMA, values).append(") ");
        }
    }

    public void serializeInsert(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> columns,
            List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
        templates.serializeInsert(metadata, entity, columns, values, subQuery, this);
    }

    void serializeForInsert(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> columns,
            List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
        this.entity = entity;

        serialize(Position.START, metadata.getFlags());

        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getInsertInto());
        }
        serialize(Position.AFTER_SELECT, metadata.getFlags());

        dmlWithSchema = true;
        handle(entity);
        dmlWithSchema = false;
        // columns
        if (!columns.isEmpty()) {
            append(" (");
            skipParent = true;
            handle(COMMA, columns);
            skipParent = false;
            append(")");
        }

        if (subQuery != null) {
            append("\n");
            serialize(subQuery.getMetadata(), false);

        } else {
            if (!useLiterals) {
                for (int i = 0; i < columns.size(); i++) {
                    if (values.get(i) instanceof Constant<?>) {
                        constantPaths.add(columns.get(i));
                    }
                }
            }

            // values
            append(templates.getValues());
            append("(");
            handle(COMMA, values);
            append(")");
        }

    }

    public void serializeUpdate(QueryMetadata metadata, RelationalPath<?> entity,
            List<Pair<Path<?>, Expression<?>>> updates) {
        templates.serializeUpdate(metadata, entity, updates, this);
    }

    void serializeForUpdate(QueryMetadata metadata, RelationalPath<?> entity,
            List<Pair<Path<?>, Expression<?>>> updates) {
        this.entity = entity;

        serialize(Position.START, metadata.getFlags());

        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getUpdate());
        }
        serialize(Position.AFTER_SELECT, metadata.getFlags());

        dmlWithSchema = true;
        handle(entity);
        dmlWithSchema = false;
        append("\n");
        append(templates.getSet());
        boolean first = true;
        skipParent = true;
        for (final Pair<Path<?>,Expression<?>> update : updates) {
            if (!first) {
                append(COMMA);
            }
            handle(update.getFirst());
            append(" = ");
            if (!useLiterals && update.getSecond() instanceof Constant<?>) {
                constantPaths.add(update.getFirst());
            }
            handle(update.getSecond());
            first = false;
        }
        skipParent = false;

        if (metadata.getWhere() != null) {
            append(templates.getWhere()).handle(metadata.getWhere());
        }
    }

    private void serializeSources(List<JoinExpression> joins) {
        if (joins.isEmpty()) {
            String dummyTable = templates.getDummyTable();
            if (!Strings.isNullOrEmpty(dummyTable)) {
                append(templates.getFrom());
                append(dummyTable);
            }
        } else {
            append(templates.getFrom());
            for (int i = 0; i < joins.size(); i++) {
                final JoinExpression je = joins.get(i);
                if (je.getFlags().isEmpty()) {
                    if (i > 0) {
                        append(templates.getJoinSymbol(je.getType()));
                    }
                    handleJoinTarget(je);
                    if (je.getCondition() != null) {
                        append(templates.getOn()).handle(je.getCondition());
                    }
                } else {
                    serialize(JoinFlag.Position.START, je.getFlags());
                    if (!serialize(JoinFlag.Position.OVERRIDE, je.getFlags()) && i > 0) {
                        append(templates.getJoinSymbol(je.getType()));
                    }
                    serialize(JoinFlag.Position.BEFORE_TARGET, je.getFlags());
                    handleJoinTarget(je);
                    serialize(JoinFlag.Position.BEFORE_CONDITION, je.getFlags());
                    if (je.getCondition() != null) {
                        append(templates.getOn()).handle(je.getCondition());
                    }
                    serialize(JoinFlag.Position.END, je.getFlags());
                }
            }
        }
    }

    public void serializeUnion(Expression<?> union, QueryMetadata metadata, boolean unionAll) {
        final List<? extends Expression<?>> groupBy = metadata.getGroupBy();
        final Predicate having = metadata.getHaving();
        final List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        final Set<QueryFlag> flags = metadata.getFlags();
        final boolean hasFlags = !flags.isEmpty();

        // union
        Stage oldStage = stage;
        handle(union);

        // group by
        if (!groupBy.isEmpty()) {
            stage = Stage.GROUP_BY;
            if (hasFlags) {
                serialize(Position.BEFORE_GROUP_BY, flags);
            }
            append(templates.getGroupBy()).handle(COMMA, groupBy);
            if (hasFlags) {
                serialize(Position.AFTER_GROUP_BY, flags);
            }
        }

        // having
        if (having != null) {
            stage = Stage.HAVING;
            if (hasFlags) {
                serialize(Position.BEFORE_HAVING, flags);
            }
            append(templates.getHaving()).handle(having);
            if (hasFlags) {
                serialize(Position.AFTER_HAVING, flags);
            }
        }

        // order by
        if (hasFlags) {
            serialize(Position.BEFORE_ORDER, flags);
        }
        if (!orderBy.isEmpty()) {
            stage = Stage.ORDER_BY;
            append(templates.getOrderBy());
            boolean first = true;
            skipParent = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first) {
                    append(COMMA);
                }
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? templates.getAsc() : templates.getDesc());
                first = false;
            }
            skipParent = false;
            if (hasFlags) {
                serialize(Position.AFTER_ORDER, flags);
            }
        }

        // end
        if (hasFlags) {
            serialize(Position.END, flags);
        }

        // reset stage
        stage = oldStage;
    }

    @Override
    public void visitConstant(Object constant) {
        if (useLiterals) {
            if (constant instanceof Collection) {
                append("(");
                boolean first = true;
                for (Object o : ((Collection)constant)) {
                    if (!first) {
                        append(COMMA);
                    }
                    append(templates.asLiteral(o));
                    first = false;
                }
                append(")");
            } else {
                append(templates.asLiteral(constant));
            }
        } else if (constant instanceof Collection) {
            append("(");
            boolean first = true;
            for (Object o : ((Collection)constant)) {
                if (!first) {
                    append(COMMA);
                }
                append("?");
                constants.add(o);
                if (first && (constantPaths.size() < constants.size())) {
                    constantPaths.add(null);
                }
                first = false;
            }
            append(")");

            int size = ((Collection)constant).size() - 1;
            Path<?> lastPath = constantPaths.get(constantPaths.size()-1);
            for (int i = 0; i < size; i++) {
                constantPaths.add(lastPath);
            }
        } else {
            append("?");
            constants.add(constant);
            if (constantPaths.size() < constants.size()) {
                constantPaths.add(null);
            }
        }
    }

    @Override
    public Void visit(ParamExpression<?> param, Void context) {
        append("?");
        constants.add(param);
        if (constantPaths.size() < constants.size()) {
            constantPaths.add(null);
        }
        return null;
    }

    @Override
    public Void visit(Path<?> path, Void context) {
        if (dml) {
            if (path.equals(entity) && path instanceof RelationalPath<?>) {
                Pair<String, String> schemaAndTable = getSchemaAndTable((RelationalPath<?>) path);
                if (dmlWithSchema && templates.isPrintSchema()) {
                    appendSchemaName(schemaAndTable.getFirst());
                    append(".");
                }
                appendTableName(schemaAndTable.getSecond());
                return null;
            } else if (entity.equals(path.getMetadata().getParent()) && skipParent) {
                appendAsColumnName(path);
                return null;
            }
        }
        final PathMetadata<?> metadata = path.getMetadata();
        if (metadata.getParent() != null && !skipParent) {
            visit(metadata.getParent(), context);
            append(".");
        }
        appendAsColumnName(path);
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> query, Void context) {
        if (inUnion && !templates.isUnionsWrapped()) {
            serialize(query.getMetadata(), false);
        } else {
            append("(");
            serialize(query.getMetadata(), false);
            append(")");
        }
        return null;
    }

    @Override
    public Void visit(TemplateExpression<?> expr, Void context) {
        if (inJoin && templates.isFunctionJoinsWrapped()) {
            append("table(");
            super.visit(expr, context);
            append(")");
        } else {
            super.visit(expr, context);
        }
        return null;
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<? extends Expression<?>> args) {
        if (args.size() == 2
         && !useLiterals
         && args.get(0) instanceof Path<?>
         && args.get(1) instanceof Constant<?>
         && operator != Ops.NUMCAST) {
            for (Element element : templates.getTemplate(operator).getElements()) {
                if (element instanceof Template.ByIndex && ((Template.ByIndex)element).getIndex() == 1) {
                    constantPaths.add((Path<?>)args.get(0));
                    break;
                }
            }
        }

        if (operator == SQLOps.UNION || operator == SQLOps.UNION_ALL) {
            boolean oldUnion = inUnion;
            inUnion = true;
            super.visitOperation(type, operator, args);
            inUnion = oldUnion;

        } else if (operator == Ops.LIKE && args.get(1) instanceof Constant) {
            final String escape = String.valueOf(templates.getEscapeChar());
            final String escaped = args.get(1).toString().replace(escape, escape + escape);
            super.visitOperation(String.class, Ops.LIKE,
                    ImmutableList.of(args.get(0), ConstantImpl.create(escaped)));

        } else if (operator == Ops.STRING_CAST) {
            final String typeName = templates.getTypeForCast(String.class);
            super.visitOperation(String.class, SQLOps.CAST,
                    ImmutableList.of(args.get(0), ConstantImpl.create(typeName)));

        } else if (operator == Ops.NUMCAST) {
            final Class<?> targetType = (Class<?>) ((Constant<?>) args.get(1)).getConstant();
            final String typeName = templates.getTypeForCast(targetType);
            super.visitOperation(targetType, SQLOps.CAST,
                    ImmutableList.of(args.get(0), ConstantImpl.create(typeName)));

        } else if (operator == Ops.ALIAS) {
            if (stage == Stage.SELECT || stage == Stage.FROM) {
                super.visitOperation(type, operator, args);
            } else {
                // handle only target
                handle(args.get(1));
            }

        } else if (operator == SQLOps.WITH_COLUMNS) {
            boolean oldSkipParent = skipParent;
            skipParent = true;
            super.visitOperation(type, operator, args);
            skipParent = oldSkipParent;

        } else {
            super.visitOperation(type, operator, args);
        }
    }

    public void setUseLiterals(boolean useLiterals) {
        this.useLiterals = useLiterals;
    }

}
