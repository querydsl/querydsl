package com.querydsl.sql;

import java.util.Date;

import com.querydsl.sql.*;
import com.querydsl.sql.mysql.MySQLQuery;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.expr.Wildcard;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExtendedSQLTest {

    public static class QAuthor extends RelationalPathBase<QAuthor> {

        private static final long serialVersionUID = -512402580246687292L;

        public static final QAuthor author = new QAuthor("author");

        public final NumberPath<Integer> id = createNumber("id", Integer.class);

        public final StringPath firstName = createString("firstName");

        public final StringPath lastName = createString("lastName");

        public QAuthor(String variable) {
            super(QAuthor.class, PathMetadataFactory.forVariable(variable), "", "AUTHOR");
            addMetadata();
        }

        protected void addMetadata() {
            addMetadata(id, ColumnMetadata.named("ID"));
            addMetadata(firstName, ColumnMetadata.named("FIRST_NAME"));
            addMetadata(lastName, ColumnMetadata.named("LAST_NAME"));
        }

    }

    public static class QBook extends RelationalPathBase<QBook> {

        private static final long serialVersionUID = 4842689279054229095L;

        public static final QBook book = new QBook("book");

        public final NumberPath<Integer> authorId = createNumber("authorId", Integer.class);

        public final StringPath language = createString("language");

        public final DatePath<Date> published = createDate("published", Date.class);

        public QBook(String variable) {
            super(QBook.class, PathMetadataFactory.forVariable(variable), "", "BOOK");
            addMetadata();
        }

        protected void addMetadata() {
            addMetadata(authorId, ColumnMetadata.named("AUTHOR_ID"));
            addMetadata(language, ColumnMetadata.named("LANGUAGE"));
            addMetadata(published, ColumnMetadata.named("PUBLISHED"));
        }

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

        SQLSerializer serializer = new SQLSerializer(new Configuration(new MySQLTemplates()));
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
