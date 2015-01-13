/*
 * Copyright 2012, Mysema Ltd
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

import java.util.List;

/**
 * Extracts the first path that occurs in an expression via breadth first search
 * 
 * @author tiwe
 *
 */
public final class PathExtractor implements Visitor<Path<?>,Void> {
      
    public static final PathExtractor DEFAULT = new PathExtractor();
    
    private PathExtractor() {}

    @Override
    public Path<?> visit(Constant<?> expr, Void context) {
        return null;
    }

    @Override
    public Path<?> visit(FactoryExpression<?> expr, Void context) {
        return visit(expr.getArgs());
    }

    @Override
    public Path<?> visit(Operation<?> expr, Void context) {
        return visit(expr.getArgs());
    }

    @Override
    public Path<?> visit(ParamExpression<?> expr, Void context) {
        return null;
    }

    @Override
    public Path<?> visit(Path<?> expr, Void context) {
        return expr;
    }

    @Override
    public Path<?> visit(SubQueryExpression<?> expr, Void context) {
        return null;
    }

    @Override
    public Path<?> visit(TemplateExpression<?> expr, Void context) {
        return visit(expr.getArgs());
    }

    private Path<?> visit(List<?> exprs) {
        for (Object e : exprs) {
            if (e instanceof Expression) {
                Path<?> path = ((Expression<?>)e).accept(this, null);
                if (path != null) {
                    return path;
                }    
            }            
        }
        return null;
    }

}
