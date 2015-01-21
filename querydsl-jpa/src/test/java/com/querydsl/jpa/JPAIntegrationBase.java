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
package com.querydsl.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.querydsl.jpa.impl.JPAProvider;
import com.querydsl.jpa.impl.JPAUtil;
import com.querydsl.jpa.testutil.JPATestRunner;

import antlr.RecognitionException;
import antlr.TokenStreamException;

@RunWith(JPATestRunner.class)
public class JPAIntegrationBase extends ParsingTest implements JPATest {

    @Rule
    public static MethodRule targetRule = new TargetRule();
    
    @Rule
    public static MethodRule hibernateOnly = new JPAProviderRule();
        
    private EntityManager em;

    private JPQLTemplates templates;
    
    @Override
    protected QueryHelper query() {
        return new QueryHelper(templates) {
            @Override
            public void parse() throws RecognitionException, TokenStreamException {
                JPQLSerializer serializer = new JPQLSerializer(templates);
                serializer.serialize(getMetadata(), false, null);
                Query query = em.createQuery(serializer.toString());
                JPAUtil.setConstants(query, serializer.getConstantToLabel(), getMetadata().getParams());
                try {
                    query.getResultList();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
        this.templates = JPAProvider.getTemplates(em);
    }

}
