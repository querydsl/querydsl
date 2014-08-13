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
package com.mysema.query.jdo.test.domain.sql;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SBook is a Querydsl query type for SBook
 */
//@Table(value="BOOK")
public class SBook extends RelationalPathBase<SBook> {

    private static final long serialVersionUID = -1566558053;

    public static final SBook book = new SBook("BOOK");

    public final StringPath author = createString("AUTHOR");

    public final NumberPath<Long> bookId = createNumber("BOOK_ID", Long.class);

    public final StringPath isbn = createString("ISBN");

    public final StringPath publisher = createString("PUBLISHER");

    public final PrimaryKey<SBook> sysIdx65 = createPrimaryKey(bookId);

    public final ForeignKey<SProduct> bookFk1 = new ForeignKey<SProduct>(this, bookId, "PRODUCT_ID");

    public SBook(String variable) {
        super(SBook.class, forVariable(variable), "", "BOOK");
    }

    public SBook(BeanPath<? extends SBook> entity) {
        super(entity.getType(),entity.getMetadata(), "", "BOOK");
    }

    public SBook(PathMetadata<?> metadata) {
        super(SBook.class, metadata, "", "BOOK");
    }

}

