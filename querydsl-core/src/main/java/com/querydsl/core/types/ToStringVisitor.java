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
package com.querydsl.core.types;

import java.util.Arrays;
import java.util.List;

/**
 * ToStringVisitor is used for toString() serialization in {@link Expression} implementations.
 *
 * @author tiwe
 */
public final class ToStringVisitor implements Visitor<String,Templates> {

    public static final ToStringVisitor DEFAULT = new ToStringVisitor();

    private ToStringVisitor() {}

    @Override
    public String visit(Constant<?> e, Templates templates) {
        return e.getConstant().toString();
    }

    @Override
    public String visit(FactoryExpression<?> e, Templates templates) {
        final StringBuilder builder = new StringBuilder();
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
        final Template template = templates.getTemplate(o.getOperator());
        if (template != null) {
            final int precedence = templates.getPrecedence(o.getOperator());
            final StringBuilder builder = new StringBuilder();
            for (Template.Element element : template.getElements()) {
                final Object rv = element.convert(o.getArgs());
                if (rv instanceof Expression) {
                    if (precedence > -1 && rv instanceof Operation) {
                        if (precedence < templates.getPrecedence(((Operation<?>)rv).getOperator())) {
                            builder.append("(");
                            builder.append(((Expression)rv).accept(this, templates));
                            builder.append(")");
                            continue;
                        }
                    }
                    builder.append(((Expression)rv).accept(this, templates));
                } else {
                    builder.append(rv.toString());
                }
            }
            return builder.toString();
        } else {
            return "unknown operation with operator " + o.getOperator().getId() + " and args " + o.getArgs();
        }
    }

    @Override
    public String visit(ParamExpression<?> param, Templates templates) {
        return "{" + param.getName() + "}";
    }

    @Override
    public String visit(Path<?> p, Templates templates) {
        final Path<?> parent = p.getMetadata().getParent();
        final Object elem = p.getMetadata().getElement();
        if (parent != null) {
            Template pattern = templates.getTemplate(p.getMetadata().getPathType());
            if (pattern != null) {
                final List<?> args = Arrays.asList(parent, elem);
                final StringBuilder builder = new StringBuilder();
                for (Template.Element element : pattern.getElements()) {
                    Object rv = element.convert(args);
                    if (rv instanceof Expression) {
                        builder.append(((Expression)rv).accept(this, templates));
                    } else {
                        builder.append(rv.toString());
                    }
                }
                return builder.toString();
            } else {
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
        final StringBuilder builder = new StringBuilder();
        for (Template.Element element : expr.getTemplate().getElements()) {
            Object rv = element.convert(expr.getArgs());
            if (rv instanceof Expression) {
                builder.append(((Expression)rv).accept(this, templates));
            } else {
                builder.append(rv.toString());
            }
        }
        return builder.toString();
    }

}
