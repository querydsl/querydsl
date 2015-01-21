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
package com.querydsl.apt;

import javax.lang.model.type.TypeMirror;

/**
 * UnsupportedTypeException is thrown for unsupported types
 * 
 * @author tiwe
 *
 */
public class UnsupportedTypeException extends APTException {

    private static final long serialVersionUID = 1082936662325717262L;

    public UnsupportedTypeException(TypeMirror mirror) {
        super("Unsupported type " + mirror);
    }
}
