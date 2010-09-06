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
public final class ToStringVisitor implements Visitor<String,Templates>{
    
    public static final ToStringVisitor DEFAULT = new ToStringVisitor();

    private ToStringVisitor(){}
    
    @Override
    public String visit(Custom<?> expr, Templates templates) {
        StringBuilder builder = new StringBuilder();
        for (Template.Element element : expr.getTemplate().getElements()){
            if (element.getStaticText() != null){
                builder.append(element.getStaticText());
            }else{
                builder.append(expr.getArg(element.getIndex()));
            }
        }
        return builder.toString();
    }

    @Override
    public String visit(Constant<?> e, Templates templates) {
        return e.getConstant().toString();
    }

    @Override
    public String visit(FactoryExpression<?> e, Templates templates) {
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
        return builder.toString();
    }

    @Override
    public String visit(Operation<?> o, Templates templates) {
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
            return builder.toString();
        } else {
            return "unknown operation with args " + o.getArgs();
        }
    }

    @Override
    public String visit(Path<?> p, Templates templates) {
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
                return builder.toString();
            }else{
                throw new IllegalArgumentException("No pattern for " + p.getMetadata().getPathType());
            }
        } else {
            return expr.toString();
        }
    }

    @Override
    public String visit(SubQueryExpression<?> expr, Templates templates) {
        return expr.getMetadata().toString();
    }

    @Override
    public String visit(Param<?> param, Templates templates) {
        return "{" + param.getName() + "}";
    }

}
