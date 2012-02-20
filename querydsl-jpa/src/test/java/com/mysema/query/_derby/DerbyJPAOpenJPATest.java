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
package com.mysema.query._derby;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.mysema.query.AbstractJPATest;
import com.mysema.query.Target;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.OpenJPATemplates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@Ignore
@RunWith(JPATestRunner.class)
@JPAConfig("derby-openjpa")
public class DerbyJPAOpenJPATest extends AbstractJPATest{

    @Override
    protected JPQLTemplates getTemplates(){
        return OpenJPATemplates.DEFAULT;
    }

    @Override
    protected Target getTarget() {
        return Target.DERBY;
    }

    @Override
    public void test(){
        // FIXME
    }
    
    @Override
    public void TupleProjection(){
        // FIXME : custom projections don't work
    }

    @Override
    public void ArrayProjection(){
        // FIXME : custom projections don't work
    }
    
}
