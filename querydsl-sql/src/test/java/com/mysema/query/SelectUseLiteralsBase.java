/*
 * Copyright 2013, Mysema Ltd
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
package com.mysema.query;

public class SelectUseLiteralsBase extends SelectBase {

    public SelectUseLiteralsBase() {
        configuration.setUseLiterals(true);
    }

    @Override
    public void Limit_and_Offset2() {
        // not supported
    }

    @Override
    public void Path_Alias() {
        // not supported
    }

}
