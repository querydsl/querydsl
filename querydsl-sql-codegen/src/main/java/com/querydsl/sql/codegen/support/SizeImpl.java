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
package com.querydsl.sql.codegen.support;

import java.lang.annotation.Annotation;

import javax.validation.Payload;
import javax.validation.constraints.Size;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("all")
public class SizeImpl implements Size {
    
    private final int min, max;
    
    public SizeImpl(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @Override
    public String message() {
        return "{javax.validation.constraints.Size.message}";
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Size.class;
    }

    @Override
    public int max() {
        return max;
    }

    @Override
    public int min() {
        return min;
    }

}
