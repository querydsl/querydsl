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

import java.sql.Types;
import java.util.*;

import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.SerializerBase;
import com.querydsl.core.types.*;
import com.querydsl.core.types.Template.Element;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.types.Null;

/**
 * {@code SqlSerializer} serializes SQL clauses into SQL
 *
 * @author tiwe
 */
public class SQLSerializer extends SerializerBase<SQLSerializer> {

    protected enum Stage { SELECT, FROM, WHERE, GROUP_BY, HAVING, ORDER_BY, MODIFIERS }

    private static final Expression<?> Q = Expressions.template(Object.class, "?");

    private static final String COMMA = ", ";

    private final LinkedList<Path<?>> constantPaths = new LinkedList<Path<?>>();

    private final List<Object> constants = new ArrayList<Object>();

    private final Set<Path<?>> withAliases = Sets.newHashSet();

    private final boolean dml;

    protected Stage stage = Stage.SELECT;

    private boolean skipParent;

    private boolean dmlWithSchema;

    private RelationalPath<?> entity;

    private final Configuration configuration;

    private final SQLTemplates templates;

    private boolean inUnion = false;

    private boolean inJoin = false;

    private boolean inSubquery = false;

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

    protected void appendAsColumnName(Path<?> path, boolean precededByDot) {
        String column = ColumnMetadata.getName(path);
        if (path.getMetadata().getParent() instanceof RelationalPath) {
            RelationalPath<?> parent = (RelationalPath<?>) path.getMetadata().getParent();
            column = configuration.getColumnOverride(parent.getSchemaAndTable(), column);
        }
        append(templates.quoteIdentifier(column, precededByDot));
    }

    private SchemaAndTable getSchemaAndTable(RelationalPath<?> path) {
        return configuration.getOverride(path.getSchemaAndTable());
    }

    protected void appendSchemaName(String schema) {
        append(templates.quoteIdentifier(schema));
    }

    protected void appendTableName(String table, boolean precededByDot) {
        append(templates.quoteIdentifier(table, precededByDot));
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
     * @return identifier columns
     */
    @SuppressWarnings("unchecked")
    private List<Expression<?>> getIdentifierColumns(List<JoinExpression> joins, boolean alias) {
        if (joins.size() == 1) {
            JoinExpression join = joins.get(0);
            if (join.getTarget() instanceof RelationalPath) {
                return ((RelationalPath) join.getTarget()).getColumns();
            } else {
                return Collections.emptyList();
            }

        } else {
            List<Expression<?>> rv = Lists.newArrayList();
            int counter = 0;
            for (JoinExpression join : joins) {
                if (join.getTarget() instanceof RelationalPath) {
                    RelationalPath path = (RelationalPath) join.getTarget();
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

    public final SQLSerializer handleSelect(final String sep, final List<? extends Expression<?>> expressions) {
        if (inSubquery) {
            Set<String> names = Sets.newHashSet();
            List<Expression<?>> replacements = Lists.newArrayList();
            for (Expression<?> expr : expressions) {
                if (expr instanceof Path) {
                    String name = ColumnMetadata.getName((Path<?>) expr);
                    if (!names.add(name.toLowerCase())) {
                        expr = ExpressionUtils.as(expr, "col__" + name + replacements.size());
                    }
                }
                replacements.add(expr);
            }
            return handle(sep, replacements);
        } else {
            return handle(sep, expressions);
        }
    }

    protected void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof RelationalPath && templates.isSupportsAlias()) {
            final RelationalPath<?> pe = (RelationalPath<?>) je.getTarget();
            if (pe.getMetadata().getParent() == null) {
                if (withAliases.contains(pe)) {
                    appendTableName(pe.getMetadata().getName(), false);
                    append(templates.getTableAlias());
                } else {
                    SchemaAndTable schemaAndTable = getSchemaAndTable(pe);
                    boolean precededByDot;
                    if (templates.isPrintSchema()) {
                        appendSchemaName(schemaAndTable.getSchema());
                        append(".");
                        precededByDot = true;
                    } else {
                        precededByDot = false;
                    }
                    appendTableName(schemaAndTable.getTable(), precededByDot);
                    append(templates.getTableAlias());
                }
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
        boolean oldInSubquery = inSubquery;
        inSubquery = inSubquery || getLength() > 0;
        boolean oldSkipParent = skipParent;
        skipParent = false;
        final Expression<?> select = metadata.getProjection();
        final List<JoinExpression> joins = metadata.getJoins();
        final Predicate where = metadata.getWhere();
        final List<? extends Expression<?>> groupBy = metadata.getGroupBy();
        final Predicate having = metadata.getHaving();
        final List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        final Set<QueryFlag> flags = metadata.getFlags();
        final boolean hasFlags = !flags.isEmpty();
        String suffix = null;

        List<? extends Expression<?>> sqlSelect;
        if (select instanceof FactoryExpression) {
            sqlSelect = ((FactoryExpression<?>) select).getArgs();
        } else if (select != null) {
            sqlSelect = ImmutableList.of(select);
        } else {
            sqlSelect = ImmutableList.of();
        }

        // with
        if (hasFlags) {
            List<Expression<?>> withFlags = Lists.newArrayList();
            boolean recursive = false;
            for (QueryFlag flag : flags) {
                if (flag.getPosition() == Position.WITH) {
                    if (flag.getFlag() == SQLTemplates.RECURSIVE) {
                        recursive = true;
                        continue;
                    }
                    withFlags.add(flag.getFlag());
                }
            }
            if (!withFlags.isEmpty()) {
                if (recursive) {
                    append(templates.getWithRecursive());
                } else {
                    append(templates.getWith());
                }
                handle(", ", withFlags);
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
                if (!groupBy.isEmpty()) {
                    append(templates.getFrom());
                    append("(");
                    append(templates.getSelect());
                    append("1 as one ");
                    suffix = ") internal";
                }

            } else {
                List<? extends Expression<?>> columns;
                if (sqlSelect.isEmpty()) {
                    columns = getIdentifierColumns(joins, !templates.isCountDistinctMultipleColumns());
                } else {
                    columns = sqlSelect;
                }
                if (!groupBy.isEmpty()) {
                    // select count(*) from (select distinct ...)
                    append(templates.getCountStar());
                    append(templates.getFrom());
                    append("(");
                    append(templates.getSelectDistinct());
                    handleSelect(COMMA, columns);
                    suffix = ") internal";
                } else if (columns.size() == 1) {
                    append(templates.getDistinctCountStart());
                    handle(columns.get(0));
                    append(templates.getDistinctCountEnd());
                } else if (templates.isCountDistinctMultipleColumns()) {
                    append(templates.getDistinctCountStart());
                    append("(").handleSelect(COMMA, columns).append(")");
                    append(templates.getDistinctCountEnd());
                } else {
                    // select count(*) from (select distinct ...)
                    append(templates.getCountStar());
                    append(templates.getFrom());
                    append("(");
                    append(templates.getSelectDistinct());
                    handleSelect(COMMA, columns);
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

            handleSelect(COMMA, sqlSelect);
        }
        if (hasFlags) {
            serialize(Position.AFTER_PROJECTION, flags);
        }

        // from
        stage = Stage.FROM;
        serializeSources(joins);

        // where
        if (hasFlags) {
            serialize(Position.BEFORE_FILTERS, flags);
        }
        if (where != null) {
            stage = Stage.WHERE;
            append(templates.getWhere()).handle(where);
        }
        if (hasFlags) {
            serialize(Position.AFTER_FILTERS, flags);
        }

        // group by
        if (hasFlags) {
            serialize(Position.BEFORE_GROUP_BY, flags);
        }
        if (!groupBy.isEmpty()) {
            stage = Stage.GROUP_BY;
            append(templates.getGroupBy()).handle(COMMA, groupBy);
        }
        if (hasFlags) {
            serialize(Position.AFTER_GROUP_BY, flags);
        }

        // having
        if (hasFlags) {
            serialize(Position.BEFORE_HAVING, flags);
        }
        if (having != null) {
            stage = Stage.HAVING;
            append(templates.getHaving()).handle(having);
        }
        if (hasFlags) {
            serialize(Position.AFTER_HAVING, flags);
        }

        // order by
        if (hasFlags) {
            serialize(Position.BEFORE_ORDER, flags);
        }
        if (!orderBy.isEmpty() && !forCountRow) {
            stage = Stage.ORDER_BY;
            append(templates.getOrderBy());
            handleOrderBy(orderBy);
        }
        if (hasFlags) {
            serialize(Position.AFTER_ORDER, flags);
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
        skipParent = oldSkipParent;
        inSubquery = oldInSubquery;
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
        this.entity = entity;
        templates.serializeDelete(metadata, entity, this);
    }

    void serializeForDelete(QueryMetadata metadata, RelationalPath<?> entity) {
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
        this.entity = entity;
        templates.serializeMerge(metadata, entity, keys, columns, values, subQuery, this);
    }

    void serializeForMerge(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> keys,
            List<Path<?>> columns, List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
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
        this.entity = entity;
        templates.serializeInsert(metadata, entity, columns, values, subQuery, this);
    }


    public void serializeInsert(QueryMetadata metadata, RelationalPath<?> entity, List<SQLInsertBatch> batches) {
        this.entity = entity;
        templates.serializeInsert(metadata, entity, batches, this);
    }

    void serializeForInsert(QueryMetadata metadata, RelationalPath<?> entity, List<SQLInsertBatch> batches) {
        serializeForInsert(metadata, entity, batches.get(0).getColumns(), batches.get(0).getValues(), null);
        for (int i = 1; i < batches.size(); i++) {
            append(COMMA);
            append("(");
            handle(COMMA, batches.get(i).getValues());
            append(")");
        }
    }

    void serializeForInsert(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> columns,
            List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
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

            if (!values.isEmpty()) {
                // values
                append(templates.getValues());
                append("(");
                handle(COMMA, values);
                append(")");
            } else {
                append(templates.getDefaultValues());
            }
        }

    }

    public void serializeUpdate(QueryMetadata metadata, RelationalPath<?> entity,
            Map<Path<?>, Expression<?>> updates) {
        templates.serializeUpdate(metadata, entity, updates, this);
    }

    void serializeForUpdate(QueryMetadata metadata, RelationalPath<?> entity,
            Map<Path<?>, Expression<?>> updates) {
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
        for (final Map.Entry<Path<?>,Expression<?>> update : updates.entrySet()) {
            if (!first) {
                append(COMMA);
            }
            handle(update.getKey());
            append(" = ");
            if (!useLiterals && update.getValue() instanceof Constant<?>) {
                constantPaths.add(update.getKey());
            }
            handle(update.getValue());
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

        // with
        if (hasFlags) {
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

        // union
        Stage oldStage = stage;
        handle(union);

        // group by
        if (hasFlags) {
            serialize(Position.BEFORE_GROUP_BY, flags);
        }
        if (!groupBy.isEmpty()) {
            stage = Stage.GROUP_BY;
            append(templates.getGroupBy()).handle(COMMA, groupBy);
        }
        if (hasFlags) {
            serialize(Position.AFTER_GROUP_BY, flags);
        }

        // having
        if (hasFlags) {
            serialize(Position.BEFORE_HAVING, flags);
        }
        if (having != null) {
            stage = Stage.HAVING;
            append(templates.getHaving()).handle(having);
        }
        if (hasFlags) {
            serialize(Position.AFTER_HAVING, flags);
        }

        // order by
        if (hasFlags) {
            serialize(Position.BEFORE_ORDER, flags);
        }
        if (!orderBy.isEmpty()) {
            stage = Stage.ORDER_BY;
            append(templates.getOrderBy());
            skipParent = true;
            handleOrderBy(orderBy);
            skipParent = false;
        }
        if (hasFlags) {
            serialize(Position.AFTER_ORDER, flags);
        }

        // end
        if (hasFlags) {
            serialize(Position.END, flags);
        }

        // reset stage
        stage = oldStage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void visitConstant(Object constant) {
        if (useLiterals) {
            if (constant instanceof Collection) {
                append("(");
                boolean first = true;
                for (Object o : ((Collection) constant)) {
                    if (!first) {
                        append(COMMA);
                    }
                    append(configuration.asLiteral(o));
                    first = false;
                }
                append(")");
            } else {
                append(configuration.asLiteral(constant));
            }
        } else if (constant instanceof Collection) {
            append("(");
            boolean first = true;
            for (Object o : ((Collection) constant)) {
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

            int size = ((Collection) constant).size() - 1;
            Path<?> lastPath = constantPaths.peekLast();
            for (int i = 0; i < size; i++) {
                constantPaths.add(lastPath);
            }
        } else {
            if (stage == Stage.SELECT
                && !Null.class.isInstance(constant)
                && configuration.getTemplates().isWrapSelectParameters()) {
                String typeName = configuration.getTypeNameForCast(constant.getClass());
                Expression type = Expressions.constant(typeName);
                super.visitOperation(constant.getClass(), SQLOps.CAST, ImmutableList.<Expression<?>>of(Q, type));
            } else {
                append("?");
            }
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
                SchemaAndTable schemaAndTable = getSchemaAndTable((RelationalPath<?>) path);
                boolean precededByDot;
                if (dmlWithSchema && templates.isPrintSchema()) {
                    appendSchemaName(schemaAndTable.getSchema());
                    append(".");
                    precededByDot = true;
                } else {
                    precededByDot = false;
                }
                appendTableName(schemaAndTable.getTable(), precededByDot);
                return null;
            } else if (entity.equals(path.getMetadata().getParent()) && skipParent) {
                appendAsColumnName(path, false);
                return null;
            }
        }
        final PathMetadata metadata = path.getMetadata();
        boolean precededByDot;
        if (metadata.getParent() != null && (!skipParent || dml)) {
            visit(metadata.getParent(), context);
            append(".");
            precededByDot = true;
        } else {
            precededByDot = false;
        }
        appendAsColumnName(path, precededByDot);
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> query, Void context) {
        boolean oldInSubsuery = inSubquery;
        inSubquery = true;
        if (inUnion && !templates.isUnionsWrapped()) {
            serialize(query.getMetadata(), false);
        } else {
            append("(");
            serialize(query.getMetadata(), false);
            append(")");
        }
        inSubquery = oldInSubsuery;
        return null;
    }

    @Override
    public Void visit(TemplateExpression<?> expr, Void context) {
        if (expr.equals(Expressions.TRUE)) {
            append(templates.serialize("1", Types.BOOLEAN));
        } else if (expr.equals(Expressions.FALSE)) {
            append(templates.serialize("0", Types.BOOLEAN));
        } else if (inJoin && expr instanceof RelationalFunctionCall
                && templates.isFunctionJoinsWrapped()) {
            append("table(");
            super.visit(expr, context);
            append(")");
        } else {
            super.visit(expr, context);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void visitOperation(Class<?> type, Operator operator, List<? extends Expression<?>> args) {
        boolean pathAdded = false;
        if (args.size() == 2
         && !useLiterals
         && args.get(0) instanceof Path<?>
         && args.get(1) instanceof Constant<?>
         && operator != Ops.NUMCAST) {
            Object constant = ((Constant<?>) args.get(1)).getConstant();
            if (!Collection.class.isInstance(constant) || !((Collection) constant).isEmpty()) {
                for (Element element : templates.getTemplate(operator).getElements()) {
                    if (element instanceof Template.ByIndex && ((Template.ByIndex) element).getIndex() == 1) {
                        constantPaths.add((Path<?>) args.get(0));
                        pathAdded = true;
                        break;
                    }
                }
            }
        }

        if (operator == Ops.SET && args.get(0) instanceof SubQueryExpression) {
            boolean oldUnion = inUnion;
            inUnion = true;
            super.visitOperation(type, SQLOps.UNION, args);
            inUnion = oldUnion;

        } else if (operator == SQLOps.UNION || operator == SQLOps.UNION_ALL) {
            boolean oldUnion = inUnion;
            inUnion = true;
            super.visitOperation(type, operator, args);
            inUnion = oldUnion;

        } else if (operator == Ops.LIKE && args.get(1) instanceof Constant<?>) {
            final String escape = String.valueOf(templates.getEscapeChar());
            final String escaped = args.get(1).toString().replace(escape, escape + escape);
            super.visitOperation(String.class, Ops.LIKE,
                    ImmutableList.of(args.get(0), ConstantImpl.create(escaped)));

        } else if (operator == Ops.STRING_CAST) {
            final String typeName = configuration.getTypeNameForCast(String.class);
            super.visitOperation(String.class, SQLOps.CAST,
                    ImmutableList.of(args.get(0), ConstantImpl.create(typeName)));

        } else if (operator == Ops.NUMCAST) {
            @SuppressWarnings("unchecked") //this is the second argument's type
            Constant<Class<?>> expectedConstant = (Constant<Class<?>>) args.get(1);

            final Class<?> targetType = expectedConstant.getConstant();
            final String typeName = configuration.getTypeNameForCast(targetType);
            super.visitOperation(targetType, SQLOps.CAST,
                    ImmutableList.of(args.get(0), ConstantImpl.create(typeName)));

        } else if (operator == Ops.ALIAS) {
            if (stage == Stage.SELECT || stage == Stage.FROM) {
                if (args.get(1) instanceof Path && !((Path<?>) args.get(1)).getMetadata().isRoot()) {
                    Path<?> path = (Path<?>) args.get(1);
                    args = ImmutableList.of(args.get(0),
                            ExpressionUtils.path(path.getType(), path.getMetadata().getName()));
                }
                super.visitOperation(type, operator, args);
            } else {
                // handle only target
                handle(args.get(1));
            }

        } else if ((operator == Ops.IN || operator == Ops.NOT_IN)
                && args.get(0) instanceof Path<?>
                && args.get(1) instanceof Constant<?>) {
            //The type of the constant expression is compatible with the left
            //expression, since the compile time checking mandates it to be.
            @SuppressWarnings("unchecked")
            Collection<Object> coll = ((Constant<Collection<Object>>) args.get(1)).getConstant();
            if (coll.isEmpty()) {
                super.visitOperation(type, operator == Ops.IN ? Ops.EQ : Ops.NE,
                        ImmutableList.of(Expressions.ONE, Expressions.TWO));
            } else {
                if (templates.getListMaxSize() == 0 || coll.size() <= templates.getListMaxSize()) {
                    super.visitOperation(type, operator, args);
                } else {
                    //The type of the path is compatible with the constant
                    //expression, since the compile time checking mandates it to be
                    @SuppressWarnings("unchecked")
                    Expression<Object> path = (Expression<Object>) args.get(0);
                    if (pathAdded) {
                        constantPaths.removeLast();
                    }
                    Iterable<List<Object>> partitioned = Iterables
                            .partition(coll, templates.getListMaxSize());
                    Predicate result;
                    if (operator == Ops.IN) {
                        result = ExpressionUtils.inAny(path, partitioned);
                    } else {
                        result = ExpressionUtils.notInAny(path, partitioned);
                    }
                    append("(");
                    result.accept(this, null);
                    append(")");
                }
            }

        } else if (operator == SQLOps.WITH_COLUMNS) {
            boolean oldSkipParent = skipParent;
            skipParent = true;
            super.visitOperation(type, operator, args);
            skipParent = oldSkipParent;

        } else if (operator == Ops.ORDER) {
            List<OrderSpecifier<?>> order = ((Constant<List<OrderSpecifier<?>>>) args.get(0)).getConstant();
            handleOrderBy(order);

        } else {
            super.visitOperation(type, operator, args);
        }

        if (operator == SQLOps.WITH_ALIAS || operator == SQLOps.WITH_COLUMNS) {
            if (args.get(0) instanceof Path) {
                withAliases.add((Path<?>) args.get(0));
            } else {
                withAliases.add((Path<?>) ((Operation<?>) args.get(0)).getArg(0));
            }
        }
    }

    public void setUseLiterals(boolean useLiterals) {
        this.useLiterals = useLiterals;
    }

    protected void setSkipParent(boolean b) {
        skipParent = b;
    }

    protected void setDmlWithSchema(boolean b) {
        dmlWithSchema = b;
    }

}
