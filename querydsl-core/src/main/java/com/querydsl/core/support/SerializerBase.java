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
package com.querydsl.core.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.Template;
import com.querydsl.core.types.TemplateExpression;
import com.querydsl.core.types.Templates;
import com.querydsl.core.types.Visitor;

/**
 * SerializerBase is a stub for Serializer implementations which serialize querydsl metadata to Strings
 *
 * @author tiwe
 */
public abstract class SerializerBase<S extends SerializerBase<S>> implements Visitor<Void,Void> {

    private final StringBuilder builder = new StringBuilder(128);
           
    private String constantPrefix = "a";

    private String paramPrefix = "p";

    private String anonParamPrefix = "_";

    private Map<Object,String> constantToLabel;

    @SuppressWarnings("unchecked")
    private final S self = (S) this;

    private final Templates templates;

    private final char escape;

    private boolean normalize = true;
    
    private boolean strict = true;
    
    public SerializerBase(Templates templates) {
        this.templates = templates;
        this.escape = templates.getEscapeChar();
    }
    
    public final S prepend(final String str) {
        builder.insert(0, str);
        return self;
    }
    
    public final S insert(int position, String str) {
        builder.insert(position, str);
        return self;
    }

    public final S append(final String str) {
        builder.append(str);
        return self;
    }

    protected String getConstantPrefix() {
        return constantPrefix;
    }

    public Map<Object,String> getConstantToLabel() {
        if (constantToLabel == null) {
            constantToLabel = new HashMap<Object,String>(4);   
        }
        return constantToLabel;
    }
    
    public int getLength() {
        return builder.length();
    }

    protected final Template getTemplate(Operator<?> op) {
        return templates.getTemplate(op);
    }

    public final S handle(Expression<?> expr) {
        expr.accept(this, null);
        return self;
    }
    
    public final S handle(Object arg) {
        if (arg instanceof Expression) {
            ((Expression)arg).accept(this, null);
        } else {
            visitConstant(arg);
        }
        return self;
    }

    public final S handle(JoinFlag joinFlag) {
        return handle(joinFlag.getFlag());
    }

    public final S handle(final String sep, final Expression<?>[] expressions) {
        for (int i = 0; i< expressions.length; i++) {
            if (i != 0) {
                append(sep);
            }
            handle(expressions[i]);
        }
        return self;
    }
    
    public final S handle(final String sep, final List<?> expressions) {
        for (int i = 0; i < expressions.size(); i++) {
            if (i != 0) {
                append(sep);
            }
            handle((Expression<?>)expressions.get(i));
        }
        return self;
    }

    protected void handleTemplate(final Template template, final List<?> args) {
        for (final Template.Element element : template.getElements()) {
            final Object rv = element.convert(args);
            if (rv instanceof Expression) {                    
                ((Expression)rv).accept(this, null);
            } else if (element.isString()) {
                builder.append(rv.toString());
            } else {
                visitConstant(rv);
            }
        }
    }

    public final boolean serialize(final QueryFlag.Position position, final Set<QueryFlag> flags) {
        boolean handled = false;
        for (final QueryFlag flag : flags) {
            if (flag.getPosition() == position) {
                handle(flag.getFlag());
                handled = true;
            }
        }
        return handled;
    }
    
    public final boolean serialize(final JoinFlag.Position position, final Set<JoinFlag> flags) {
        boolean handled = false;
        for (final JoinFlag flag : flags) {
            if (flag.getPosition() == position) {
                handle(flag.getFlag());
                handled = true;
            }
        }
        return handled;
    }

    public void setConstantPrefix(String prefix) {
        this.constantPrefix = prefix;
    }

    public void setParamPrefix(String prefix) {
        this.paramPrefix = prefix;
    }

    public void setAnonParamPrefix(String prefix) {
        this.anonParamPrefix = prefix;
    }
    
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;       
    }
    
    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    @Override
    public String toString() {
        if (normalize) {
            return Normalization.normalize(builder.toString());    
        } else {
            return builder.toString();
        }        
    }

    @Override
    public final Void visit(Constant<?> expr, Void context) {
        visitConstant(expr.getConstant());
        return null;
    }
    
    public void visitConstant(Object constant) {
        if (!getConstantToLabel().containsKey(constant)) {
            final String constLabel = constantPrefix + (getConstantToLabel().size() + 1);
            getConstantToLabel().put(constant, constLabel);
            append(constLabel);
        } else {
            append(getConstantToLabel().get(constant));
        }
    }

    @Override
    public Void visit(ParamExpression<?> param, Void context) {
        String paramLabel;
        if (param.isAnon()) {
            paramLabel = anonParamPrefix + param.getName();
        } else {
            paramLabel = paramPrefix + param.getName();
        }
        getConstantToLabel().put(param, paramLabel);
        append(paramLabel);
        return null;
    }

    @Override
    public Void visit(TemplateExpression<?> expr, Void context) {
        handleTemplate(expr.getTemplate(), expr.getArgs());
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Void context) {
        handle(", ", expr.getArgs());
        return null;
    }

    @Override
    public Void visit(Operation<?> expr, Void context) {
        visitOperation(expr.getType(), expr.getOperator(), expr.getArgs());
        return null;
    }

    @Override
    public Void visit(Path<?> path, Void context) {
        final PathType pathType = path.getMetadata().getPathType();
        final Template template = templates.getTemplate(pathType);
        final Object element = path.getMetadata().getElement();        
        List<Object> args;
        if (path.getMetadata().getParent() != null) {
            args = ImmutableList.of(path.getMetadata().getParent(), element);
        } else {
            args = ImmutableList.of(element);
        }
        handleTemplate(template, args);
        return null;
    }
    
    protected void visitOperation(Class<?> type, Operator<?> operator, final List<? extends Expression<?>> args) {
        final Template template = templates.getTemplate(operator);
        if (template != null) {
            final int precedence = templates.getPrecedence(operator);        
            for (final Template.Element element : template.getElements()) {
                final Object rv = element.convert(args);
                if (rv instanceof Expression) {
                    final Expression<?> expr = (Expression<?>) rv;
                    if (precedence > -1 && expr instanceof Operation) {
                        if (precedence < templates.getPrecedence(((Operation<?>) expr).getOperator())) {
                            append("(").handle(expr).append(")");
                        } else {
                            handle(expr);
                        }
                    } else {
                        handle(expr);
                    }
                } else if (element.isString()) {
                    append(rv.toString());
                } else {
                    visitConstant(rv);
                }
            }    
        } else if (strict) {
            throw new IllegalArgumentException("Got no pattern for " + operator);
        } else {
            append(operator.toString());
            append("(");
            handle(", ", args);
            append(")");
        }        
    }

}
