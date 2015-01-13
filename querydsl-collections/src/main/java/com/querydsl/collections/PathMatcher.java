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
package com.querydsl.collections;

import static org.hamcrest.core.IsNull.notNullValue;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Function;
import com.querydsl.core.types.Path;

/**
 * Matches based on the current value of a path.
 * 
 * @author Jeroen van Schagen
 * @author tiwe
 *
 * @param <T> type of the path root
 * @param <V> type of value being matched
 */
public class PathMatcher<T, V> extends TypeSafeDiagnosingMatcher<T> {   
    
    private final Function<T, V> accessor;
    
    private final Matcher<? super V> matcher;
    
    private final Path<V> path;
    
    public PathMatcher(Path<V> path, Matcher<? super V> matcher) {
        this(path, matcher, GuavaHelpers.<T,V>wrap(path));
    }
    
    public PathMatcher(Path<V> path, Matcher<? super V> matcher, Function<T, V> accessor) {
        this.path = path;
        this.matcher = matcher;
        this.accessor = accessor;
    }
    
    @Factory
    public static <T,P> Matcher<T> hasValue(Path<P> path) {
        return new PathMatcher<T,P>(path, notNullValue());
    }
  
    @Factory
    public static <T,P> Matcher<T> hasValue(Path<P> path, Matcher<? super P> matcher) {
        return new PathMatcher<T,P>(path, matcher);
    }
    
    @Override
    protected boolean matchesSafely(T bean, Description mismatchDescription) {
        V value = accessor.apply(bean);
        boolean valueMatches = matcher.matches(value);
        if (!valueMatches) {
            mismatchDescription.appendText("value \"" + path.toString() + "\" ");
            matcher.describeMismatch(value, mismatchDescription);
        }
        return valueMatches;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("valueOf(");
        description.appendValue(path.toString());
        description.appendText(", ");
        description.appendDescriptionOf(matcher);
        description.appendText(")");
    }
    
}
