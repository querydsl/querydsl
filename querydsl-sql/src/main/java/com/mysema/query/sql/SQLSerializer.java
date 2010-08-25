/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Pair;
import com.mysema.query.JoinExpression;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.types.*;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ExprConst;

/**
 * SqlSerializer serializes Querydsl queries into SQL
 *
 * @author tiwe
 * @version $Id$
 */
public class SQLSerializer extends SerializerBase<SQLSerializer> {

    private final SerializationContext context = new SerializationContext(){

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
            Expr<?>[] exprs = new Expr[args.length];
            for (int i = 0; i < args.length; i++){
                exprs[i] = ExprConst.create(args[i]);
            }
            SQLSerializer.this.handle(CSimple.create(Object.class, template, exprs));

        }

    };

    private static final String COMMA = ", ";

    private final List<Object> constants = new ArrayList<Object>();

    private final boolean dml;

    private boolean skipParent;

    private RelationalPath<?> entity;

    private final SQLTemplates templates;

    public SQLSerializer(SQLTemplates templates) {
        this(templates, false);
    }

    public SQLSerializer(SQLTemplates templates, boolean dml) {
        super(templates);
        this.templates = templates;
        this.dml = dml;
    }

    private void appendAsColumnName(Path<?> path){
        String column = path.getMetadata().getExpression().toString();
        append(templates.quoteColumnName(column));
    }

    private void appendAsTableName(Path<?> path){
        String table = path.getAnnotatedElement().getAnnotation(Table.class).value();
        append(templates.quoteTableName(table));
    }

    public List<Object> getConstants(){
        return constants;
    }

    @SuppressWarnings("unchecked")
    private List<Expr<?>> getIdentifierColumns(List<JoinExpression> joins) {
        JoinExpression join = joins.get(0);
        RelationalPath path = (RelationalPath)join.getTarget();
        if (path.getPrimaryKey() != null){
            return path.getPrimaryKey().getLocalColumns();
        }else{
            return path.getColumns();
        }        
    }

    protected SQLTemplates getTemplates(){
        return templates;
    }

    @SuppressWarnings("unchecked")
    private void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof RelationalPath && templates.isSupportsAlias()) {
            RelationalPath<?> pe = (RelationalPath<?>) je.getTarget();
            if (pe.getMetadata().getParent() == null) {
                appendAsTableName(pe);
                append(templates.getTableAlias());
            }
        }
        handle(je.getTarget());
    }

    public void serialize(QueryMetadata metadata, boolean forCountRow){
        templates.serialize(metadata, forCountRow, context);
    }

    @SuppressWarnings("unchecked")
    private void serializeForQuery(QueryMetadata metadata, boolean forCountRow) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        Set<QueryFlag> flags = metadata.getFlags();

        List<Expr<?>> sqlSelect = new ArrayList<Expr<?>>();
        for (Expr<?> selectExpr : select) {
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
        if (forCountRow) {
            append(templates.getSelect());            
            serialize(Position.AFTER_SELECT, flags);
            
            if (!metadata.isDistinct()){
                append(templates.getCountStar());
            }else{
                append(templates.getDistinctCountStart());
                if (sqlSelect.isEmpty()){
                    handle(COMMA, getIdentifierColumns(joins));
                }else{
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
        serializeSources(joins);

        // where            
        serialize(Position.BEFORE_FILTERS, flags);
        if (where != null) {            
            append(templates.getWhere()).handle(where);
            serialize(Position.AFTER_FILTERS, flags);
        }        

        // group by
        serialize(Position.BEFORE_GROUP_BY, flags);
        if (!groupBy.isEmpty()) {            
            append(templates.getGroupBy()).handle(COMMA, groupBy);
            serialize(Position.AFTER_GROUP_BY, flags);
        }

        // having
        serialize(Position.BEFORE_HAVING, flags);
        if (having != null) {
            if (groupBy.isEmpty()) {                
                throw new IllegalArgumentException("having, but not groupBy was given");
            }            
            append(templates.getHaving()).handle(having);
            serialize(Position.AFTER_HAVING, flags);
        }
        
        // order by
        serialize(Position.BEFORE_ORDER, flags);
        if (!orderBy.isEmpty() && !forCountRow) {
            append(templates.getOrderBy());                       
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first){
                    append(COMMA);
                }
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? templates.getAsc() : templates.getDesc());
                first = false;
            }
            serialize(Position.AFTER_ORDER, flags);
        }
        
        // end
        serialize(Position.END, flags);

    }

    public void serializeForDelete(QueryMetadata metadata, RelationalPath<?> entity) {
        this.entity = entity;
        
        serialize(Position.START, metadata.getFlags());
        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())){
            append(templates.getDeleteFrom());    
        }        
        handle(entity.asExpr());
        if (metadata.getWhere() != null) {
            skipParent = true;
            append(templates.getWhere()).handle(metadata.getWhere());
            skipParent = false;
        }
        
        serialize(Position.END, metadata.getFlags());
    }

    public void serializeForMerge(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> keys,
            List<Path<?>> columns, List<Expr<?>> values, @Nullable SubQuery<?> subQuery) {
        this.entity = entity;
        
        serialize(Position.START, metadata.getFlags());
        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())){
            append(templates.getMergeInto());    
        }        
        handle(entity.asExpr());
        append(" ");
        // columns
        if (!columns.isEmpty()){
            skipParent = true;
            append("(").handle(COMMA, columns).append(") ");
            skipParent = false;
        }
        // keys
        if (!keys.isEmpty()){
            append(templates.getKey());
            skipParent = true;
            append("(").handle(COMMA, keys).append(") ");
            skipParent = false;
        }

        if (subQuery != null){
            // subquery
            append("\n");
            serialize(subQuery.getMetadata(), false);
        }else{
            // values
            append(templates.getValues());
            append("(").handle(COMMA, values).append(") ");
        }
        
        serialize(Position.END, metadata.getFlags());
    }

    public void serializeForInsert(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> columns,
            List<Expr<?>> values, @Nullable SubQuery<?> subQuery) {
        this.entity = entity;
        
        serialize(Position.START, metadata.getFlags());
        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())){
            append(templates.getInsertInto());    
        }        
        handle(entity.asExpr());
        // columns
        if (!columns.isEmpty()){
            append("(");
            skipParent = true;
            handle(COMMA, columns);
            skipParent = false;
            append(")");
        }

        if (subQuery != null){
            append("\n");
            serialize(subQuery.getMetadata(), false);
        }else{
            // values
            append(templates.getValues());
            append("(");
            handle(COMMA, values);
            append(")");
        }
        
        serialize(Position.END, metadata.getFlags());
    }

    public void serializeForUpdate(QueryMetadata metadata, RelationalPath<?> entity, List<Pair<Path<?>, ?>> updates) {
        this.entity = entity;
        
        serialize(Position.START, metadata.getFlags());
        
        if (!serialize(Position.START_OVERRIDE, metadata.getFlags())){
            append(templates.getUpdate());    
        }        
        handle(entity.asExpr());
        append("\n");
        append(templates.getSet());
        boolean first = true;
        skipParent = true;
        for (Pair<Path<?>,?> update : updates){
            if (!first){
                append(COMMA);
            }
            handle(update.getFirst().asExpr());
            append(" = ");
            if (update.getSecond() instanceof Expr<?>){
                handle((Expr<?>)update.getSecond());
            }else{
                handle(ExprConst.create(update.getSecond()));
            }
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
            if (!StringUtils.isEmpty(dummyTable)){
                append(templates.getFrom());
                append(dummyTable);
            }
        }else{
            append(templates.getFrom());
            for (int i = 0; i < joins.size(); i++) {
                JoinExpression je = joins.get(i);
                if (i > 0) {
                    append(templates.getJoinSymbol(je.getType()));
                }
                handleJoinTarget(je);
                if (je.getCondition() != null) {
                    append(templates.getOn()).handle(je.getCondition());
                }
                for (Object flag : je.getFlags()){
                    append(flag.toString());
                }
            }    
        }        
    }

    @SuppressWarnings("unchecked")
    public void serializeUnion(SubQuery[] sqs, List<OrderSpecifier<?>> orderBy) {
        // union
        handle(templates.getUnion(), (List)Arrays.asList(sqs));

        // order by
        if (!orderBy.isEmpty()) {
            append(templates.getOrderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first){
                    append(COMMA);
                }
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? templates.getAsc() : templates.getDesc());
                first = false;
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void visit(Constant<?> expr) {
        if (expr.getConstant() instanceof Collection){
            append("(");
            boolean first = true;
            for (Object o : ((Collection)expr.getConstant())){
                if (!first){
                    append(COMMA);
                }
                append("?");
                constants.add(o);
                first = false;
            }
            append(")");
        }else{
            append("?");
            constants.add(expr.getConstant());
        }
    }

    @Override
    public void visit(Param<?> param){
        append("?");
        constants.add(param);
    }

    public void visit(Path<?> path) {
        if (dml){
            if (path.equals(entity)){
                appendAsTableName(path);
            }else if (entity.equals(path.getMetadata().getParent()) && skipParent){
                appendAsColumnName(path);
            }else{
                super.visit(path);
            }
        }else{
            super.visit(path);
        }
    }

    @Override
    public void visit(SubQuery<?> query) {
        append("(");
        serialize(query.getMetadata(), false);
        append(")");
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expr<?>> args) {
        if (operator.equals(Ops.STRING_CAST)) {
            String typeName = templates.getTypeForClass(String.class);
            visitOperation(String.class, SQLTemplates.CAST, Arrays.<Expr<?>>asList(args.get(0), ExprConst.create(typeName)));

        } else if (operator.equals(Ops.NUMCAST)) {
            Class<?> targetType = (Class<?>) ((Constant<?>) args.get(1)).getConstant();
            String typeName = templates.getTypeForClass(targetType);
            visitOperation(targetType, SQLTemplates.CAST, Arrays.<Expr<?>>asList(args.get(0), ExprConst.create(typeName)));

        } else {
            super.visitOperation(type, operator, args);
        }
    }

}
