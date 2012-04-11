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
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.mysema.commons.lang.Pair;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.Expressions;
import com.mysema.query.support.SerializerBase;
import com.mysema.query.types.Constant;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.TemplateExpressionImpl;

/**
 * SqlSerializer serializes Querydsl queries into SQL
 *
 * @author tiwe
 */
public class SQLSerializer extends SerializerBase<SQLSerializer> {
    
    protected enum Stage {SELECT, FROM, WHERE, GROUP_BY, HAVING, ORDER_BY}

    private final SerializationContext context = new SerializationContext() {

        @Override
        public void serialize(QueryMetadata metadata, boolean forCountRow) {
            SQLSerializer.this.serializeForQuery(metadata, forCountRow);
        }

        @Override
        public SerializationContext append(String str) {
            SQLSerializer.this.append(str);
            return this;
        }

        @Override
        public void handle(String template, Object... args) {
            Expression<?>[] exprs = new Expression[args.length];
            for (int i = 0; i < args.length; i++) {
                exprs[i] = new ConstantImpl<Object>(args[i]);
            }
            SQLSerializer.this.handle(TemplateExpressionImpl.create(Object.class, template, exprs));

        }

    };

    private static final String COMMA = ", ";

    private final List<Path<?>> constantPaths = new ArrayList<Path<?>>();
    
    private final List<Object> constants = new ArrayList<Object>();

    private final boolean dml;
    
    protected Stage stage = Stage.SELECT;

    private boolean skipParent;

    private boolean dmlWithSchema;
    
    private RelationalPath<?> entity;

    private final SQLTemplates templates;
    
    public SQLSerializer(SQLTemplates templates) {
        this(templates, false, false);
    }
    
    public SQLSerializer(SQLTemplates templates, boolean dml) {
        this(templates, dml, false);
    }

    public SQLSerializer(SQLTemplates templates, boolean dml, boolean dry) {
        super(templates, dry);
        this.templates = templates;
        this.dml = dml;
    }

    private void appendAsColumnName(Path<?> path) {
        String column = path.getMetadata().getExpression().toString();
        append(templates.quoteIdentifier(column));
    }
    
    private void appendAsSchemaName(RelationalPath<?> path) {
        String schema = path.getSchemaName();
        append(templates.quoteIdentifier(schema));
    }

    private void appendAsTableName(RelationalPath<?> path) {
        String table = path.getTableName();
        append(templates.quoteIdentifier(table));
    }

    public List<Object> getConstants() {
        return constants;
    }
    
    public List<Path<?>> getConstantPaths() {
        return constantPaths;
    }

    @SuppressWarnings("unchecked")
    private List<Expression<?>> getIdentifierColumns(List<JoinExpression> joins) {
        JoinExpression join = joins.get(0);
        @SuppressWarnings("rawtypes")
        RelationalPath path = (RelationalPath)join.getTarget();
        if (path.getPrimaryKey() != null) {
            return path.getPrimaryKey().getLocalColumns();
        } else {
            return path.getColumns();
        }        
    }

    protected SQLTemplates getTemplates() {
        return templates;
    }

    private void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof RelationalPath && templates.isSupportsAlias()) {
            RelationalPath<?> pe = (RelationalPath<?>) je.getTarget();
            if (pe.getMetadata().getParent() == null) {
                if (templates.isPrintSchema()) {
                    appendAsSchemaName(pe);
                    append(".");
                }     
                appendAsTableName(pe);
                append(templates.getTableAlias());
            }
        }
        handle(je.getTarget());
    }

    public void serialize(QueryMetadata metadata, boolean forCountRow) {
        templates.serialize(metadata, forCountRow, context);
    }

    @SuppressWarnings("unchecked")
    private void serializeForQuery(QueryMetadata metadata, boolean forCountRow) {
        List<? extends Expression<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
        Predicate where = metadata.getWhere();
        List<? extends Expression<?>> groupBy = metadata.getGroupBy();
        Predicate having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        Set<QueryFlag> flags = metadata.getFlags();

        List<Expression<?>> sqlSelect = new ArrayList<Expression<?>>();
        for (Expression<?> selectExpr : select) {
            if (selectExpr instanceof FactoryExpression) {
                // transforms constructor arguments into individual select expressions
                sqlSelect.addAll(((FactoryExpression<?>) selectExpr).getArgs());
            } else {
                sqlSelect.add(selectExpr);
            }
        }
        
        
        // start
        serialize(Position.START, flags);

        // select
        Stage oldStage = stage;
        stage = Stage.SELECT;
        if (forCountRow) {
            append(templates.getSelect());            
            serialize(Position.AFTER_SELECT, flags);
            
            if (!metadata.isDistinct()) {
                append(templates.getCountStar());
            } else {
                append(templates.getDistinctCountStart());
                if (sqlSelect.isEmpty()) {
                    handle(COMMA, getIdentifierColumns(joins));
                } else {
                    handle(COMMA, sqlSelect);
                }
                append(templates.getDistinctCountEnd());
            }

        } else if (!sqlSelect.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(templates.getSelect());
            } else {
                append(templates.getSelectDistinct());
            }
            serialize(Position.AFTER_SELECT, flags);
            
            handle(COMMA, sqlSelect);
        }
        serialize(Position.AFTER_PROJECTION, flags);

        // from
        stage = Stage.FROM;
        serializeSources(joins);
        
        // where          
        stage = Stage.WHERE;
        serialize(Position.BEFORE_FILTERS, flags);
        if (where != null) {            
            append(templates.getWhere()).handle(where);
            serialize(Position.AFTER_FILTERS, flags);
        }        

        // group by
        stage = Stage.GROUP_BY;
        serialize(Position.BEFORE_GROUP_BY, flags);
        if (!groupBy.isEmpty()) {            
            append(templates.getGroupBy()).handle(COMMA, groupBy);
            serialize(Position.AFTER_GROUP_BY, flags);
        }

        // having
        stage = Stage.HAVING;
        serialize(Position.BEFORE_HAVING, flags);
        if (having != null) {
            append(templates.getHaving()).handle(having);
            serialize(Position.AFTER_HAVING, flags);
        }
        
        // order by
        stage = Stage.ORDER_BY;
        serialize(Position.BEFORE_ORDER, flags);
        if (!orderBy.isEmpty() && !forCountRow) {
            append(templates.getOrderBy());                       
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first) {
                    append(COMMA);
                }
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? templates.getAsc() : templates.getDesc());
                first = false;
            }
            serialize(Position.AFTER_ORDER, flags);
        }
        

        if (!forCountRow && metadata.getModifiers().isRestricting()) {
            templates.serializeModifiers(metadata, context);
        }
        
        // end
        serialize(Position.END, flags);
        
        // reset stage
        stage = oldStage;

    }

    public void serializeForDelete(QueryMetadata metadata, RelationalPath<?> entity) {
        this.entity = entity;
        serialize(Position.START, metadata.getFlags());        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getDeleteFrom());    
        }        
        dmlWithSchema = true;
        handle(entity);
        dmlWithSchema = false;
        
        if (metadata.getWhere() != null) {
            append(templates.getWhere()).handle(metadata.getWhere());
        }        
        serialize(Position.END, metadata.getFlags());
        
    }

    public void serializeForMerge(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> keys,
            List<Path<?>> columns, List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
        this.entity = entity;
        
        serialize(Position.START, metadata.getFlags());
        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getMergeInto());    
        }        
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
            for (int i = 0; i < columns.size(); i++) {
                if (values.get(i) instanceof Constant<?>) {
                    constantPaths.add(columns.get(i));
                }
            }
            
            // values
            append(templates.getValues());
            append("(").handle(COMMA, values).append(") ");
        }
        
        serialize(Position.END, metadata.getFlags());
    }

    public void serializeForInsert(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> columns,
            List<Expression<?>> values, @Nullable SubQueryExpression<?> subQuery) {
        this.entity = entity;
        
        serialize(Position.START, metadata.getFlags());
        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getInsertInto());    
        }        
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
            for (int i = 0; i < columns.size(); i++) {
                if (values.get(i) instanceof Constant<?>) {
                    constantPaths.add(columns.get(i));
                }
            }
            
            // values
            append(templates.getValues());
            append("(");
            handle(COMMA, values);
            append(")");            
        }
        
        serialize(Position.END, metadata.getFlags());
        
    }

    public void serializeForUpdate(QueryMetadata metadata, RelationalPath<?> entity, 
            List<Pair<Path<?>, Expression<?>>> updates) {
        this.entity = entity;
        
        serialize(Position.START, metadata.getFlags());
        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())) {
            append(templates.getUpdate());    
        }        
        dmlWithSchema = true;
        handle(entity);
        dmlWithSchema = false;
        append("\n");
        append(templates.getSet());
        boolean first = true;
        skipParent = true;
        for (Pair<Path<?>,Expression<?>> update : updates) {
            if (!first) {
                append(COMMA);
            }                        
            handle(update.getFirst());
            append(" = ");
            if (update.getSecond() instanceof Constant<?>) {
                constantPaths.add(update.getFirst());
            }
            handle(update.getSecond());
            first = false;
        }
        skipParent = false;
        
        if (metadata.getWhere() != null) {
            append(templates.getWhere()).handle(metadata.getWhere());
        }
        
        serialize(Position.END, metadata.getFlags());
    }

    private void serializeSources(List<JoinExpression> joins) {        
        if (joins.isEmpty()) {
            String dummyTable = templates.getDummyTable();
            if (!StringUtils.isEmpty(dummyTable)) {
                append(templates.getFrom());
                append(dummyTable);
            }
        } else {
            append(templates.getFrom());
            for (int i = 0; i < joins.size(); i++) {
                JoinExpression je = joins.get(i);
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

    @SuppressWarnings("unchecked")
    public void serializeUnion(SubQueryExpression[] sqs, List<OrderSpecifier<?>> orderBy, boolean unionAll) {
        // union
        handle(unionAll ? templates.getUnionAll() : templates.getUnion(), Arrays.asList(sqs));

        // order by
        if (!orderBy.isEmpty()) {
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
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public Void visit(Constant<?> expr, Void context) {
        if (expr.getConstant() instanceof Collection) {
            append("(");
            boolean first = true;
            for (Object o : ((Collection)expr.getConstant())) {
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
            
            int size = ((Collection)expr.getConstant()).size() - 1;
            Path<?> lastPath = constantPaths.get(constantPaths.size()-1);
            for (int i = 0; i < size; i++) {
                constantPaths.add(lastPath);
            }
        } else {
            append("?");
            
            constants.add(expr.getConstant());
            if (constantPaths.size() < constants.size()) {
                constantPaths.add(null);
            }
        }
        return null;
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
                if (dmlWithSchema && templates.isPrintSchema()) {
                    appendAsSchemaName((RelationalPath<?>)path);
                    append(".");
                }                
                appendAsTableName((RelationalPath<?>)path);
                return null;
            }else if (entity.equals(path.getMetadata().getParent()) && skipParent) {
                appendAsColumnName(path);
                return null;
            }
        }
        if (path.getMetadata().getParent() != null && !skipParent) {
            visit(path.getMetadata().getParent(), context);
            append(".");
        }
        append(templates.quoteIdentifier(path.getMetadata().getExpression().toString()));    
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> query, Void context) {
        append("(");
        serialize(query.getMetadata(), false);
        append(")");
        return null;
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<? extends Expression<?>> args) {
        if (args.size() == 2 
         && args.get(0) instanceof Path<?> 
         && args.get(1) instanceof Constant<?>
         && operator != Ops.STRING_CAST 
         && operator != Ops.NUMCAST
         && operator != Ops.SUBSTR_1ARG
         && operator != Ops.CHAR_AT
         && operator != SQLTemplates.CAST) {
            constantPaths.add((Path<?>)args.get(0));
        }       
        
        if (operator.equals(Ops.STRING_CAST)) {
            String typeName = templates.getTypeForCast(String.class);
            visitOperation(String.class, SQLTemplates.CAST, Arrays.<Expression<?>>asList(args.get(0), 
                    ConstantImpl.create(typeName)));

        } else if (operator.equals(Ops.NUMCAST)) {
            Class<?> targetType = (Class<?>) ((Constant<?>) args.get(1)).getConstant();
            String typeName = templates.getTypeForCast(targetType);
            visitOperation(targetType, SQLTemplates.CAST, Arrays.<Expression<?>>asList(args.get(0), 
                    ConstantImpl.create(typeName)));

        } else if (operator.equals(Ops.ALIAS)) {
            if (stage == Stage.SELECT || stage == Stage.FROM) {
                if (args.get(0) instanceof Operation && ((Operation)args.get(0)).getOperator() == SQLTemplates.UNION) {
                    args = Arrays.asList(Expressions.operation(Object.class, Ops.WRAPPED, args.get(0)), args.get(1));
                }
                super.visitOperation(type, operator, args);
            } else {
                // handle only target
                handle(args.get(1));
            }
            
        } else {
            super.visitOperation(type, operator, args);
        }
    }

    
}
