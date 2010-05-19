/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

/**
 * ToStringVisitor is used for toString() serialization in {@link Expr} implementations.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class ToStringVisitor implements Visitor{

    private final Templates templates;

    private String toString = "?";

    public ToStringVisitor(Templates templates){
        this.templates = templates;
    }
    
    public String toString() {
        return toString;
    }

    @Override
    public void visit(Custom<?> expr) {
        StringBuilder builder = new StringBuilder();
        for (Template.Element element : expr.getTemplate().getElements()){
            if (element.getStaticText() != null){
                builder.append(element.getStaticText());
            }else{
                builder.append(expr.getArg(element.getIndex()));
            }
        }
        toString = builder.toString();
    }

    @Override
    public void visit(Constant<?> e) {
        toString = e.getConstant().toString();
    }

    @Override
    public void visit(EConstructor<?> e) {
        StringBuilder builder = new StringBuilder();
        builder.append("new ").append(e.getType().getSimpleName()).append("(");
        boolean first = true;
        for (Expr<?> arg : e.getArgs()){
            if (!first){
        	builder.append(", ");
            }
            builder.append(arg);
            first = false;
        }
        builder.append(")");
        toString = builder.toString();
    }

    @Override
    public void visit(Operation<?> o) {
        Template template = templates.getTemplate(o.getOperator());
        if (template != null) {
            StringBuilder builder = new StringBuilder();
            for (Template.Element element : template.getElements()){
                if (element.getStaticText() != null){
                    builder.append(element.getStaticText());
                }else{
                    builder.append(o.getArg(element.getIndex()));
                }
            }
            toString = builder.toString();
        } else {
            toString = "unknown operation with args " + o.getArgs();
        }
    }

    @Override
    public void visit(Path<?> p) {
        Path<?> parent = p.getMetadata().getParent();
        Expr<?> expr = p.getMetadata().getExpression();
        if (parent != null) {
            Template pattern = templates.getTemplate(p.getMetadata().getPathType());
            if (pattern != null) {
                StringBuilder builder = new StringBuilder();
                for (Template.Element element : pattern.getElements()){
                    if (element.getStaticText() != null){
                        builder.append(element.getStaticText());
                    }else if (element.getIndex() == 0){
                        builder.append(parent);
                    }else if (element.getIndex() == 1){
                        builder.append(expr);
                    }
                }
                toString = builder.toString();
            }
        } else if (expr != null) {
            toString = expr.toString();
        }
    }

    @Override
    public void visit(SubQuery<?> expr) {
        toString = expr.getMetadata().toString();        
    }

}
