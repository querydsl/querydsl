package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.mysql.MySQLQuery;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class ExtendedSQLTest {

    public static class QAuthor extends RelationalPathBase<QAuthor> {
        
        private static final long serialVersionUID = -512402580246687292L;
        
        public static final QAuthor author = new QAuthor("author");
        
        public QAuthor(String variable) {
            super(QAuthor.class, PathMetadataFactory.forVariable(variable), null, "AUTHOR");
        }
        
        public final NumberPath<Integer> id = createNumber("ID", Integer.class);
        
        public final StringPath firstName = createString("FIRST_NAME");
        
        public final StringPath lastName = createString("LAST_NAME");

    }
    
    public static class QBook extends RelationalPathBase<QBook> {

        private static final long serialVersionUID = 4842689279054229095L;

        public static final QBook book = new QBook("book");
        
        public QBook(String variable) {
            super(QBook.class, PathMetadataFactory.forVariable(variable), null, "BOOK");
        }
        
        public final NumberPath<Integer> authorId = createNumber("AUTHOR_ID", Integer.class);
                
        public final StringPath language = createString("LANGUAGE");
        
        public final DatePath<Date> published = createDate("PUBLISHED", Date.class);
        
    }
    
    @Test
    public void test() {
//        SELECT FIRST_NAME, LAST_NAME, COUNT(*)
//        FROM AUTHOR
//        JOIN BOOK ON AUTHOR.ID = BOOK.AUTHOR_ID
//       WHERE LANGUAGE = 'DE'
//         AND PUBLISHED > '2008-01-01'
//    GROUP BY FIRST_NAME, LAST_NAME
//      HAVING COUNT(*) > 5
//    ORDER BY LAST_NAME ASC NULLS FIRST
//       LIMIT 2
//      OFFSET 1
//         FOR UPDATE
//          OF FIRST_NAME, LAST_NAME
        
        QAuthor author = QAuthor.author;
        QBook book = QBook.book;
        MySQLQuery query = new MySQLQuery(null);
        query.from(author)
           .join(book).on(author.id.eq(book.authorId))
           .where(book.language.eq("DE"), book.published.eq(new Date()))
           .groupBy(author.firstName, author.lastName)
           .having(Wildcard.count.gt(5))
           .orderBy(author.lastName.asc())
           .limit(2)
           .offset(1)
           .forUpdate();
           // of(author.firstName, author.lastName)
        
        query.getMetadata().addProjection(author.firstName);
        query.getMetadata().addProjection(author.lastName);
        query.getMetadata().addProjection(Wildcard.count);

        SQLSerializer serializer = new SQLSerializer(new MySQLTemplates());
        serializer.serialize(query.getMetadata(), false);
        
        assertEquals("select author.FIRST_NAME, author.LAST_NAME, count(*)\n"+
                     "from AUTHOR author\n"+
                     "join BOOK book\n"+
                     "on author.ID = book.AUTHOR_ID\n"+
                     "where book.LANGUAGE = ? and book.PUBLISHED = ?\n"+
                     "group by author.FIRST_NAME, author.LAST_NAME\n"+
                     "having count(*) > ?\n"+
                     "order by author.LAST_NAME asc\n"+
                     "limit ?\n"+
                     "offset ?\n"+
                     "for update", serializer.toString());
           
    }
    
}
