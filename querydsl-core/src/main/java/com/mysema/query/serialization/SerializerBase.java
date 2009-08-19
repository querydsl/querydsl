/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Template;
import com.mysema.query.types.Templates;
import com.mysema.query.types.VisitorBase;
import com.mysema.query.types.Template.Element;
import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathType;

/**
 * SerializerBase is a stub for Serializer implementations
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class SerializerBase<SubType extends SerializerBase<SubType>> extends VisitorBase<SubType> {

    private final StringBuilder builder = new StringBuilder();

    protected Map<Object,String> constantToLabel = new HashMap<Object,String>();
    
    protected String constantPrefix = "a";
    
    protected final Templates templates;

    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType) this;

    public SerializerBase(Templates patterns) {
        this.templates = Assert.notNull(patterns,"patterns is null");
    }

    public SubType append(String... str) {
        for (String s : str) {
            builder.append(s);
        }
        return _this;
    }

    public Map<Object,String> getConstantToLabel() {
        return constantToLabel;
    }

    public final SubType handle(String sep, List<? extends Expr<?>> expressions) {
        boolean first = true;
        for (Expr<?> expr : expressions) {
            if (!first) {
                append(sep);
            }
            handle(expr);
            first = false;
        }
        return _this;
    }

    public void setConstantPrefix(String prefix){
        this.constantPrefix = prefix;
    }
    
    
    public String toString() {
        return builder.toString();
    }

    protected void visit(Custom<?> expr) {        
        for (Element element : expr.getTemplate().getElements()){
            if (element.getStaticText() != null){
                append(element.getStaticText());
            }else{
                handle(expr.getArg(element.getIndex()));
            }
        }
    }

    protected void visit(EArrayConstructor<?> oa) {
        append("new ").append(oa.getElementType().getName()).append("[]{");
        handle(", ", oa.getArgs()).append("}");
    }

    @Override
    protected void visit(EConstant<?> expr) {        
        if (!constantToLabel.containsKey(expr.getConstant())) {
            String constLabel = constantPrefix + (constantToLabel.size() + 1);
            constantToLabel.put(expr.getConstant(), constLabel);
            append(constLabel);
        } else {
            append(constantToLabel.get(expr.getConstant()));
        }
    }

    protected void visit(EConstructor<?> expr) {
        append("new ").append(expr.getType().getName()).append("(");
        handle(", ", expr.getArgs()).append(")");
    }

    @Override
    protected void visit(Operation<?, ?> expr) {
        visitOperation(expr.getType(), expr.getOperator(), expr.getArgs());
    }

    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        Template template = templates.getTemplate(pathType);
        List<Expr<?>> args = new ArrayList<Expr<?>>();
        if (path.getMetadata().getParent() != null){
            args.add((Expr<?>) path.getMetadata().getParent());
        }
        args.add(path.getMetadata().getExpression());
        
        for (Element element : template.getElements()){
            if (element.getStaticText() != null){
                append(element.getStaticText());
            }else if (element.isAsString()){
                append(args.get(element.getIndex()).toString());
            }else{
                handle(args.get(element.getIndex()));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expr<?>> args) {
        Template template = templates.getTemplate(operator);
        if (template == null) {
            throw new IllegalArgumentException("Got no pattern for " + operator);
        }
        int precedence = templates.getPrecedence(operator);
        for (Element element : template.getElements()){
            if (element.getStaticText() != null){
                append(element.getStaticText());
            }else if (element.isAsString()){
                append(args.get(element.getIndex()).toString());
            }else{
                int i = element.getIndex();
                boolean wrap = false;
                if (args.get(i) instanceof Operation){
                    wrap = precedence < templates.getPrecedence(((Operation<?, ?>) args.get(i)).getOperator());
                }
                if (wrap){
                    append("(");
                }
                handle(args.get(i));
                if (wrap){
                    append(")");
                }
            }
        }        
    }

}
