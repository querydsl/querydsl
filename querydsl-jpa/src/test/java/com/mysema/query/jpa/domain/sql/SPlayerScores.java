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
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SPlayerScores is a Querydsl query type for SPlayerScores
 */
public class SPlayerScores extends RelationalPathBase<SPlayerScores> implements RelationalPath<SPlayerScores> {

    private static final long serialVersionUID = -855115221;

    public static final SPlayerScores playerScores = new SPlayerScores("PLAYER_SCORES");

    public final NumberPath<Integer> element = createNumber("ELEMENT", Integer.class);

    public final NumberPath<Long> playerId = createNumber("PLAYER_ID", Long.class);

    public final ForeignKey<SPlayer> fkd5dc571ff51f2004 = new ForeignKey<SPlayer>(this, playerId, "ID");

    public SPlayerScores(String variable) {
        super(SPlayerScores.class, forVariable(variable), null, "PLAYER_SCORES");
    }

    public SPlayerScores(BeanPath<? extends SPlayerScores> entity) {
        super(entity.getType(), entity.getMetadata(), null, "PLAYER_SCORES");
    }

    public SPlayerScores(PathMetadata<?> metadata) {
        super(SPlayerScores.class, metadata, null, "PLAYER_SCORES");
    }

}

