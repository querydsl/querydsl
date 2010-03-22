/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Custom;
import com.mysema.query.types.EArrayConstructor;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathType;
import com.mysema.query.types.Template;
import com.mysema.query.types.Templates;
import com.mysema.query.types.Visitor;

/**
 * SerializerBase is a stub for Serializer implementations
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class SerializerBase<S extends SerializerBase<S>> implements Visitor {

    private static final String COMMA = ", ";

    private static final String NEW = "new ";

    private final StringBuilder builder = new StringBuilder();

    private String constantPrefix = "a";
    
    private final Map<Object,String> constantToLabel = new HashMap<Object,String>();
    
    @SuppressWarnings("unchecked")
    private final S self = (S) this;

    private final Templates templates;

    public SerializerBase(Templates patterns) {
        this.templates = Assert.notNull(patterns,"patterns is null");
    }    

    public S append(String... str) {
        for (String s : str) {
            builder.append(s);
        }
        return self;
    }
    
    protected String getConstantPrefix() {
        return constantPrefix;
    }
    
    public Map<Object,String> getConstantToLabel() {
        return constantToLabel;
    }
    
    protected Template getTemplate(Operator<?> op) {
        return templates.getTemplate(op);
    }

    public S handle(Expr<?> expr) {
        expr.accept(this);
        return self;
    }

    public final S handle(String sep, List<? extends Expr<?>> expressions) {
        boolean first = true;
        for (Expr<?> expr : expressions) {
            if (!first) {
                append(sep);
            }
            handle(expr);
            first = false;
        }
        return self;
    }

    private void handleTemplate(Template template, List<Expr<?>> args){
        for (Template.Element element : template.getElements()){
            if (element.getStaticText() != null){
                append(element.getStaticText());
            }else if (element.isAsString()){
                append(args.get(element.getIndex()).toString());
            }else if (element.hasConverter()){    
                handle(element.convert(args.get(element.getIndex())));
            }else{
                handle(args.get(element.getIndex()));    
            }
        }
    }
    
    
    public void setConstantPrefix(String prefix){
        this.constantPrefix = prefix;
    }

    public String toString() {
        return builder.toString();
    }


    @Override
    public void visit(Constant<?> expr) {        
        if (!constantToLabel.containsKey(expr.getConstant())) {
            String constLabel = constantPrefix + (constantToLabel.size() + 1);
            constantToLabel.put(expr.getConstant(), constLabel);
            append(constLabel);
        } else {
            append(constantToLabel.get(expr.getConstant()));
        }
    }

    @Override
    public void visit(Custom<?> expr) {        
        handleTemplate(expr.getTemplate(), expr.getArgs());
    }

    @Override
    public void visit(EArrayConstructor<?> oa) {
        append(NEW).append(oa.getElementType().getName()).append("[]{");
        handle(COMMA, oa.getArgs()).append("}");
    }

    @Override
    public void visit(EConstructor<?> expr) {
        append(NEW).append(expr.getType().getName()).append("(");
        handle(COMMA, expr.getArgs()).append(")");
    }
    

    @Override
    public void visit(Operation<?, ?> expr) {
        visitOperation(expr.getType(), expr.getOperator(), expr.getArgs());
    }
    
    @Override
    public void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        Template template = templates.getTemplate(pathType);
        List<Expr<?>> args = new ArrayList<Expr<?>>();
        if (path.getMetadata().getParent() != null){
            args.add((Expr<?>) path.getMetadata().getParent());
        }
        args.add(path.getMetadata().getExpression());
        handleTemplate(template, args);
    }
    
    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expr<?>> args) {
        Template template = templates.getTemplate(operator);
        if (template == null) {
            throw new IllegalArgumentException("Got no pattern for " + operator);
        }
        int precedence = templates.getPrecedence(operator);
        for (Template.Element element : template.getElements()){
            if (element.getStaticText() != null){
                append(element.getStaticText());
            }else if (element.isAsString()){
                append(args.get(element.getIndex()).toString());
            }else{
                int i = element.getIndex();
                boolean wrap = false;
                Expr arg = args.get(i);
                if (arg instanceof BooleanBuilder){
                    arg = ((BooleanBuilder)arg).getValue();
                }                
                if (arg instanceof Operation){
                    wrap = precedence < templates.getPrecedence(((Operation<?, ?>) arg).getOperator());
                }
                if (wrap){
                    append("(");
                }
                if (element.hasConverter()){
                    handle(element.convert(arg));
                }else{
                    handle(arg);                    
                }
                if (wrap){
                    append(")");
                }
            }
        }        
    }

}
