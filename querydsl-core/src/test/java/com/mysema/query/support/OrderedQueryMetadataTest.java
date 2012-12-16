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
package com.mysema.query.support;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.PathMetadataFactory;

@SuppressWarnings("all")
public class OrderedQueryMetadataTest {
    
    private Path x = new PathImpl(Object.class, PathMetadataFactory.forVariable("x"));
    private Path y = new PathImpl(Object.class, PathMetadataFactory.forVariable("y"));
    private Path x_a = new PathImpl(Object.class, PathMetadataFactory.forProperty(x, "a"));
    private Path x_a_a = new PathImpl(Object.class, PathMetadataFactory.forProperty(x_a, "a"));
    private Path x_a_b = new PathImpl(Object.class, PathMetadataFactory.forProperty(x_a, "b"));
    private Path x_b = new PathImpl(Object.class, PathMetadataFactory.forProperty(x, "a"));
    private Path y_a = new PathImpl(Object.class, PathMetadataFactory.forProperty(y, "a"));
    private Path y_b = new PathImpl(Object.class, PathMetadataFactory.forProperty(y, "b"));
    
    private void addJoin(QueryMetadata md, JoinExpression j) {
        md.addJoin(j.getType(), j.getTarget());
    }
    
    @Test
    public void AddJoin() {
        List<JoinExpression> joins = new ArrayList<JoinExpression>();
        joins.add(new JoinExpression(JoinType.DEFAULT, x));
        joins.add(new JoinExpression(JoinType.DEFAULT, y));
        joins.add(new JoinExpression(JoinType.INNERJOIN, y));
        joins.add(new JoinExpression(JoinType.INNERJOIN, x_a));
        joins.add(new JoinExpression(JoinType.INNERJOIN, x_a_a));
        joins.add(new JoinExpression(JoinType.INNERJOIN, x_a_b));
        joins.add(new JoinExpression(JoinType.INNERJOIN, x_b));
        joins.add(new JoinExpression(JoinType.INNERJOIN, y_a));
        joins.add(new JoinExpression(JoinType.INNERJOIN, y_b));

        for (JoinExpression join1 : joins) {
            for (JoinExpression join2 : joins) {
                QueryMetadata md = new OrderedQueryMetadata();
                addJoin(md, join1);
                addJoin(md, join2);
                validate(md.getJoins());
                
                for (JoinExpression join3 : joins) {
                    md = new OrderedQueryMetadata();
                    addJoin(md, join1);
                    addJoin(md, join2);
                    addJoin(md, join3);
                    validate(md.getJoins());
                    
                    for (JoinExpression join4 : joins) {
                        md = new OrderedQueryMetadata();
                        addJoin(md, join1);
                        addJoin(md, join2);
                        addJoin(md, join3);
                        addJoin(md, join4);
                        validate(md.getJoins());
                    }
                }
            }
            
        }
    }

    private void validate(List<JoinExpression> joins) {
        int maxFromIndex = -1;
        int maxJoinIndex = -1;
        
        for (int i = 0; i < joins.size(); i++) {
            if (joins.get(i).getType() == JoinType.DEFAULT) {
                maxFromIndex = i;
            } else {
                maxJoinIndex = i;
            }
        }
        
        String str = joins.toString();
        if (maxJoinIndex > -1 && maxFromIndex > -1) {
            assertTrue(str, maxJoinIndex >= maxFromIndex);    
        }        
    }

}
