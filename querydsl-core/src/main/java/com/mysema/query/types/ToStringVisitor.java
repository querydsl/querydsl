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
package com.mysema.query.types;

/**
 * ToStringVisitor is used for toString() serialization in {@link Expression} implementations.
 *
 * @author tiwe
 */
public final class ToStringVisitor implements Visitor<String,Templates>{
    
    public static final ToStringVisitor DEFAULT = new ToStringVisitor();

    private ToStringVisitor(){}
    
    @Override
    public String visit(Constant<?> e, Templates templates) {
        return e.getConstant().toString();
    }

    @Override
    public String visit(FactoryExpression<?> e, Templates templates) {
        StringBuilder builder = new StringBuilder();
        builder.append("new ").append(e.getType().getSimpleName()).append("(");
        boolean first = true;
        for (Expression<?> arg : e.getArgs()) {
            if (!first) {
                builder.append(", ");
            }
            builder.append(arg.accept(this, templates));
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
            for (Template.Element element : template.getElements()) {
                if (element.getStaticText() != null) {
                    builder.append(element.getStaticText());
                } else {
                    builder.append(o.getArg(element.getIndex()).accept(this, templates));
                }
            }
            return builder.toString();
        } else {
            return "unknown operation with args " + o.getArgs();
        }                  
    }

    @Override
    public String visit(ParamExpression<?> param, Templates templates) {
        return "{" + param.getName() + "}";
    }

    @Override
    public String visit(Path<?> p, Templates templates) {
        Path<?> parent = p.getMetadata().getParent();
        Object elem = p.getMetadata().getElement();
        if (parent != null) {
            Template pattern = templates.getTemplate(p.getMetadata().getPathType());
            if (pattern != null) {
                StringBuilder builder = new StringBuilder();
                for (Template.Element element : pattern.getElements()) {
                    if (element.getStaticText() != null) {
                        builder.append(element.getStaticText());
                    } else if (element.getIndex() == 0) {
                        builder.append(parent.accept(this, templates));
                    } else if (element.getIndex() == 1) {
                        if (elem instanceof Expression) {
                            builder.append(((Expression)elem).accept(this, templates));    
                        } else {
                            builder.append(elem.toString());
                        }                        
                    }
                }
                return builder.toString();
            }else{
                throw new IllegalArgumentException("No pattern for " + p.getMetadata().getPathType());
            }
        } else {
            return elem.toString();
        }
    }

    @Override
    public String visit(SubQueryExpression<?> expr, Templates templates) {
        return expr.getMetadata().toString();
    }

    @Override
    public String visit(TemplateExpression<?> expr, Templates templates) {
        StringBuilder builder = new StringBuilder();
        for (Template.Element element : expr.getTemplate().getElements()) {
            if (element.getStaticText() != null) {
                builder.append(element.getStaticText());
            } else {
                Object arg = expr.getArg(element.getIndex());
                if (arg instanceof Expression) {
                    builder.append(((Expression) arg).accept(this, templates));
                } else {
                    builder.append(arg.toString());
                }
            }
        }
        return builder.toString();
    }

}
