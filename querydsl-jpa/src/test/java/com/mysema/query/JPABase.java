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
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(JPATestRunner.class)
public class JPABase extends AbstractStandardTest {

    @Rule
    public static MethodRule targetRule = new TargetRule();
    
    @Rule
    public static MethodRule jpaProviderRule = new JPAProviderRule();
    
    private EntityManager entityManager;
    
    @Override
    protected JPAQuery query(){
        return new JPAQuery(entityManager);
    }
    
    protected JPADeleteClause delete(EntityPath<?> path) {
        return new JPADeleteClause(entityManager, path);
    }
    
    @Override
    protected JPAQuery testQuery(){
        return new JPAQuery(entityManager, new DefaultQueryMetadata().noValidate());
    }    
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected void save(Object entity) {
        entityManager.persist(entity);
    }
    
    @Test
    public void Finder() {
        Map<String,Object> conditions = new HashMap<String,Object>();
        conditions.put("name", "Bob123");
        
        List<Cat> cats = CustomFinder.findCustom(entityManager, Cat.class, conditions, "name");
        assertEquals(1, cats.size());
        assertEquals("Bob123", cats.get(0).getName());
    }

    @Test
    public void QueryExposure(){
        //save(new Cat(20));
        List<Cat> results = query().from(QCat.cat).createQuery(QCat.cat).getResultList();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    public void Hint(){
        javax.persistence.Query query = query().from(QCat.cat)
                .setHint("org.hibernate.cacheable", true)
                .createQuery(QCat.cat);
        assertNotNull(query);
        assertTrue(query.getHints().containsKey("org.hibernate.cacheable"));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void Hint2(){
        assertFalse(query().from(QCat.cat).setHint("org.hibernate.cacheable", true)
                .list(QCat.cat).isEmpty());
    }

    @Test
    public void LockMode(){
        javax.persistence.Query query = query().from(QCat.cat)
                .setLockMode(LockModeType.PESSIMISTIC_READ).createQuery(QCat.cat);
        assertTrue(query.getLockMode().equals(LockModeType.PESSIMISTIC_READ));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void LockMode2(){
        assertFalse(query().from(QCat.cat).setLockMode(LockModeType.PESSIMISTIC_READ)
                .list(QCat.cat).isEmpty());
    }

    @Test
    public void FlushMode() {
        assertFalse(query().from(QCat.cat).setFlushMode(FlushModeType.AUTO).list(QCat.cat).isEmpty());
    }
    
    @Test
    public void Limit1_UniqueResult(){
        assertNotNull(query().from(QCat.cat).limit(1).uniqueResult(QCat.cat));
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    public void Connection_Access(){
        assertNotNull(query().from(QCat.cat).createQuery(QCat.cat).unwrap(Connection.class));
    }
    
    @Test
    @Ignore
    public void Delete() {
        delete(QCat.cat).execute();
    }
    
    @Test
    public void Delete_Where() {
        delete(QCat.cat).where(QCat.cat.name.eq("XXX")).execute();
    }
    
    @Test
    public void Delete_Where_SubQuery_Exists() {
        QCat parent = QCat.cat;
        QCat child = new QCat("kitten");
        
        delete(child)
            .where(child.id.eq(-100), new JPASubQuery()
               .from(parent)
               .where(parent.id.eq(-200), 
                      child.in(parent.kittens)).exists())
            .execute();
    }
}
