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
package com.mysema.query._h2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.AbstractJPATest;
import com.mysema.query.Target;
import com.mysema.query.jpa.EclipseLinkTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(JPATestRunner.class)
@JPAConfig("h2-eclipselink")
public class H2JPAEclipseLinkTest extends AbstractJPATest{

    @Override
    protected JPQLTemplates getTemplates(){
        return EclipseLinkTemplates.DEFAULT;
    }

    @Override
    protected Target getTarget() {
        return Target.H2;
    }
    
    @Override
    public void Hint(){
        javax.persistence.Query query = query().from(QCat.cat)
                .setHint(QueryHints.BIND_PARAMETERS, HintValues.PERSISTENCE_UNIT_DEFAULT)
                .createQuery(QCat.cat);
        assertNotNull(query);
        assertTrue(query.getHints().containsKey(QueryHints.BIND_PARAMETERS));
        assertFalse(query.getResultList().isEmpty());
    }

    @Override
    public void Connection_Access(){
        // cannot unwrap to Connection instance
    }
    
    @Override
    public void Map_ContainsKey(){
        // not supported
    }

    @Override
    public void Map_ContainsKey2(){
        // not supported
    }
    
    @Override
    public void Map_ContainsKey3(){
        // not supported
    }
    
    @Override
    public void Map_ContainsValue(){
        // not supported
    }
    
    @Override
    public void Map_ContainsValue2(){
        // not supported
    }
    
    @Override
    public void Map_ContainsValue3(){
        // not supported
    }
    
    @Override
    public void JoinEmbeddable() {
        // not supported
    }
    
    @Override
    public void test(){
        // FIXME
    }
    
    @Override
    public void Any_And_Lt(){
        // FIXME
    }
    
    @Override
    public void Any_And_Gt(){
        // FIXME
    }
    
    @Override
    public void In5() {
        // not supported
    }
    
    @Override
    public void In7() {
        // not supported
    }
    
}
