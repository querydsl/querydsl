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
 * SerializerBase is a stub for Serializer implementations which serialize query metadata to Strings
 *
 * @author tiwe
 */
public abstract class SerializerBase<S extends SerializerBase<S>> implements Visitor<Void,Void> {

    private final StringBuilder builder = new StringBuilder();
           
    private String constantPrefix = "a";

    private String paramPrefix = "p";

    private String anonParamPrefix = "_";

    private final Map<Object,String> constantToLabel = new HashMap<Object,String>();

    @SuppressWarnings("unchecked")
    private final S self = (S) this;

    private final Templates templates;
    
    private boolean normalize = true;
    
    public SerializerBase(Templates templates) {
        this.templates = templates;
    }
    
    public final S prepend(String str) {
        builder.insert(0, str);
        return self;
    }
    
    public final S insert(int position, String str) {
        builder.insert(position, str);
        return self;
    }

    public final S append(String str) {
        builder.append(str);
        return self;
    }

    protected String getConstantPrefix() {
        return constantPrefix;
    }

    public Map<Object,String> getConstantToLabel() {
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

    public final S handle(String sep, Expression<?>[] expressions) {
        boolean first = true;
        for (Expression<?> expr : expressions) {
            if (!first) {
                append(sep);
            }
            handle(expr);
            first = false;
        }
        return self;
    }
    
    public final S handle(String sep, List<?> expressions) {
        boolean first = true;
        for (Object expr : expressions) {
            if (!first) {
                append(sep);
            }
            handle((Expression<?>)expr);
            first = false;
        }
        return self;
    }

    protected void handleTemplate(Template template, List<?> args){
        for (Template.Element element : template.getElements()) {
            if (element.getStaticText() != null) {
                append(element.getStaticText());
            } else if (element.isAsString()) {
                append(args.get(element.getIndex()).toString());
            } else if (element.hasConverter()) {
                handle(element.convert(args.get(element.getIndex())));
            } else {
                handle(args.get(element.getIndex()));                               
            }
        }
    }

    protected final boolean serialize(QueryFlag.Position position, Set<QueryFlag> flags) {
        boolean handled = false;
        for (QueryFlag flag : flags) {
            if (flag.getPosition() == position) {
                handle(flag.getFlag());
                handled = true;
            }
        }
        return handled;
    }
    
    protected final boolean serialize(JoinFlag.Position position, Set<JoinFlag> flags){
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
            String constLabel = constantPrefix + (getConstantToLabel().size() + 1);
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
        PathType pathType = path.getMetadata().getPathType();
        Template template = templates.getTemplate(pathType);
        List<Object> args = new ArrayList<Object>();
        if (path.getMetadata().getParent() != null) {
            args.add(path.getMetadata().getParent());
        }
        args.add(path.getMetadata().getElement());
        handleTemplate(template, args);
        return null;
    }
    
    protected void visitOperation(Class<?> type, Operator<?> operator, List<? extends Expression<?>> args) {
        Template template = templates.getTemplate(operator);
        if (template == null) {
            throw new IllegalArgumentException("Got no pattern for " + operator);
        }
        int precedence = templates.getPrecedence(operator);
        for (Template.Element element : template.getElements()) {
            if (element.getStaticText() != null) {
                append(element.getStaticText());                
            } else if (element.isAsString() && args.get(element.getIndex()) instanceof Constant) {
                // serialize only constants directly
                append(args.get(element.getIndex()).toString());                
            } else {
                int i = element.getIndex();
                boolean wrap = false;
                Expression<?> arg = args.get(i);
                if (arg instanceof Operation && ((Operation)arg).getOperator() == Ops.DELEGATE) {
                    arg = ((Operation)arg).getArg(0);
                }
                if (precedence > -1 && arg instanceof Operation) {
                    wrap = precedence < templates.getPrecedence(((Operation<?>) arg).getOperator());
                }
                if (element.hasConverter()) {
                    arg = element.convert(arg);
                }
                if (wrap) {
                    append("(");
                    handle(arg);
                    append(")");
                } else {
                    handle(arg);
                }                
            }
        }
    }


}
