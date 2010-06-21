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
import com.mysema.query.types.*;

/**
 * SerializerBase is a stub for Serializer implementations
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class SerializerBase<S extends SerializerBase<S>> implements Visitor {

    private final StringBuilder builder = new StringBuilder();

    private String constantPrefix = "a";

    private String paramPrefix = "p";

    private String anonParamPrefix = "_";

    private final Map<Object,String> constantToLabel = new HashMap<Object,String>();

    @SuppressWarnings("unchecked")
    private final S self = (S) this;

    private final Templates templates;

    public SerializerBase(Templates patterns) {
        this.templates = Assert.notNull(patterns,"patterns");
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

    public final S handle(String sep, List<?> expressions) {
        boolean first = true;
        for (Object expr : expressions) {
            if (!first) {
                append(sep);
            }
            if (expr instanceof Expr<?>){
                handle((Expr<?>)expr);
            }else{
                throw new IllegalArgumentException("Unsupported type " + expr.getClass().getName());
            }
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

    public void setParamPrefix(String prefix){
        this.paramPrefix = prefix;
    }

    public void setAnonParamPrefix(String prefix){
        this.anonParamPrefix = prefix;
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
    public void visit(Param<?> param){
        String paramLabel;
        if (param.isAnon()){
            paramLabel = anonParamPrefix + param.getName();
        }else{
            paramLabel = paramPrefix + param.getName();
        }
        constantToLabel.put(param, paramLabel);
        append(paramLabel);
    }

    @Override
    public void visit(Custom<?> expr) {
        handleTemplate(expr.getTemplate(), expr.getArgs());
    }

    @Override
    public void visit(EConstructor<?> expr) {
    handle(", ", expr.getArgs());
    }

    @Override
    public void visit(Operation<?> expr) {
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
                    wrap = precedence < templates.getPrecedence(((Operation<?>) arg).getOperator());
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
