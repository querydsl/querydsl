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
package com.mysema.query.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.mysema.commons.lang.Assert;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryFlag;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathType;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.Templates;
import com.mysema.query.types.Visitor;

/**
 * SerializerBase is a stub for Serializer implementations
 *
 * @author tiwe
 */
public abstract class SerializerBase<S extends SerializerBase<S>> implements Visitor<Void,Void> {

    private static final Joiner EMPTY_JOINER = Joiner.on("");

    private final StringBuilder builder = new StringBuilder();
        
    private static final Pattern OPERATION = Pattern.compile("\\d+[\\+\\-]\\d+");
    
    private String constantPrefix = "a";

    private String paramPrefix = "p";

    private String anonParamPrefix = "_";

    private final Map<Object,String> constantToLabel = new HashMap<Object,String>();

    @SuppressWarnings("unchecked")
    private final S self = (S) this;

    private final Templates templates;
    
    private final boolean dry;
    
    private boolean normalize = true;
    
    public static final String normalize(String queryString) {        
        StringBuilder rv = new StringBuilder();
        Matcher m = OPERATION.matcher(queryString);
        int end = 0;
        while (m.find()) {
            if (m.start() > end) {
                rv.append(queryString.subSequence(end, m.start()));
            }
            String str = queryString.substring(m.start(), m.end());
            String[] operands = str.split("[\\+\\-]");
            char operator = str.charAt(operands[0].length());
            if (operator == '+') {
                rv.append(Integer.valueOf(operands[0]) + Integer.valueOf(operands[1]));
            } else {
                rv.append(Integer.valueOf(operands[0]) - Integer.valueOf(operands[1]));
            }
            end = m.end();
        }
        if (end < queryString.length()) {
            rv.append(queryString.substring(end));
        }
        return rv.toString();
    }

    public SerializerBase(Templates templates) {
        this(templates, false);
    }
    
    public SerializerBase(Templates templates, boolean dry) {
        this.templates = Assert.notNull(templates,"templates");
        this.dry = dry;
    }
    
    public S prepend(String... str) {
        if (!dry){
            builder.insert(0, EMPTY_JOINER.join(str));                
        }        
        return self;
    }

    public S append(String... str) {
        if (!dry) {
            for (String s : str) {
                builder.append(s);
            }    
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

    public S handle(Expression<?> expr) {
        expr.accept(this, null);
        return self;
    }

    public S handle(JoinFlag joinFlag) {
        return handle(joinFlag.getFlag());
    }

    public final S handle(String sep, List<?> expressions) {
        boolean first = true;
        for (Object expr : expressions) {
            if (!first) {
                append(sep);
            }
            if (expr instanceof Expression<?>) {
                handle((Expression<?>)expr);
            } else {
                throw new IllegalArgumentException("Unsupported type " + expr.getClass().getName());
            }
            first = false;
        }
        return self;
    }

    private void handleTemplate(Template template, List<Expression<?>> args){
        for (Template.Element element : template.getElements()) {
            if (element.getStaticText() != null) {
                append(element.getStaticText());
            } else if (element.isAsString()) {
                appendAsString(args.get(element.getIndex()));
            } else if (element.hasConverter()) {
                handle(element.convert(args.get(element.getIndex())));
            } else {
                handle(args.get(element.getIndex()));
            }
        }
    }

    protected boolean serialize(QueryFlag.Position position, Set<QueryFlag> flags) {
        boolean handled = false;
        for (QueryFlag flag : flags) {
            if (flag.getPosition() == position) {
                handle(flag.getFlag());
                handled = true;
            }
        }
        return handled;
    }
    
    protected boolean serialize(JoinFlag.Position position, Set<JoinFlag> flags){
        boolean handled = false;
        for (JoinFlag flag : flags) {
            if (flag.getPosition() == position) {
                handle(flag.getFlag());
                handled = true;
            }
        }
        return handled;
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
    
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;       
    }

    @Override
    public String toString() {
        if (normalize) {
            return normalize(builder.toString());    
        } else {
            return builder.toString();
        }        
    }

    @Override
    public Void visit(Constant<?> expr, Void context) {
        if (!getConstantToLabel().containsKey(expr.getConstant())) {
            String constLabel = constantPrefix + (getConstantToLabel().size() + 1);
            getConstantToLabel().put(expr.getConstant(), constLabel);
            append(constLabel);
        } else {
            append(getConstantToLabel().get(expr.getConstant()));
        }
        return null;
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
        PathType pathType = path.getMetadata().getPathType();
        Template template = templates.getTemplate(pathType);
        List<Expression<?>> args = new ArrayList<Expression<?>>();
        if (path.getMetadata().getParent() != null) {
            args.add(path.getMetadata().getParent());
        }
        args.add(path.getMetadata().getExpression());
        handleTemplate(template, args);
        return null;
    }

    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator<?> operator, List<? extends Expression<?>> args) {
        Template template = templates.getTemplate(operator);
        if (template == null) {
            throw new IllegalArgumentException("Got no pattern for " + operator);
        }
        int precedence = templates.getPrecedence(operator);
        for (Template.Element element : template.getElements()) {
            if (element.getStaticText() != null) {
                append(element.getStaticText());                
            } else if (element.isAsString()) {
                appendAsString(args.get(element.getIndex()));                
            } else {
                int i = element.getIndex();
                boolean wrap = false;
                Expression arg = args.get(i);
                if (arg instanceof Operation && ((Operation)arg).getOperator() == Ops.DELEGATE) {
                    arg = ((Operation)arg).getArg(0);
                }
                if (arg instanceof Operation) {
                    wrap = precedence < templates.getPrecedence(((Operation<?>) arg).getOperator());
                }
                if (wrap) {
                    append("(");
                }
                if (element.hasConverter()) {
                    handle(element.convert(arg));
                } else {
                    handle(arg);
                }
                if (wrap) {
                    append(")");
                }
            }
        }
    }

    protected void appendAsString(Expression<?> expr) {
        append(expr.toString());
    }

}
