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
package com.querydsl.core.support;

import java.util.Collection;
import java.util.List;

import com.querydsl.core.types.*;

/**
 * Extracts the paths that occurs in an expression via breadth first search
 * 
 * @author tiwe
 *
 */
public final class PathsExtractor implements Visitor<Void, List<Path<?>>> {
      
    public static final PathsExtractor DEFAULT = new PathsExtractor();
    
    private PathsExtractor() {}

    @Override
    public Void visit(Constant<?> expr, List<Path<?>> paths) {
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, List<Path<?>> paths) {
        visit(expr.getArgs(), paths);
        return null;
    }

    @Override
    public Void visit(Operation<?> expr, List<Path<?>> paths) {
        visit(expr.getArgs(), paths);
        return null;
    }

    @Override
    public Void visit(ParamExpression<?> expr, List<Path<?>> paths) {
        return null;
    }

    @Override
    public Void visit(Path<?> expr, List<Path<?>> paths) {
        paths.add(expr);
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> expr, List<Path<?>> paths) {
        return null;
    }

    @Override
    public Void visit(TemplateExpression<?> expr, List<Path<?>> paths) {
        visit(expr.getArgs(), paths);
        return null;
    }

    public Path<?> visit(Collection<?> exprs, List<Path<?>> paths) {
        for (Object e : exprs) {
            if (e instanceof Expression) {
                ((Expression) e).accept(this, paths);
            }
        }
        return null;
    }

}
