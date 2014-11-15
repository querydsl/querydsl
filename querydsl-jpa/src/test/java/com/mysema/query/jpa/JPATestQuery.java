package com.mysema.query.jpa;

import javax.annotation.Nullable;
import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.domain.Cat;
import com.mysema.query.domain.QCat;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;

public interface JPATestQuery<T> extends SubQueryExpression<T> {

    public static class Examples {
        {
            QCat cat = QCat.cat;
            QCat other = null;
            JPATestQuery<Void> query = null;
            JPATestQuery<Void> subQuery = null;

            List<Cat> cats1 = query.select(cat).from(cat).list();
            List<Cat> cats2 = query.from(cat).select(cat).list();

            query.select(cat).from(cat)
                 .where(subQuery.select(other)
                                .where(other.ne(cat), other.name.eq(cat.name))
                                .exists())
                 .list();

            // shorter
            select(cat).from(cat)
                .where(select(other)
                      .where(other.ne(cat), other.name.eq(cat.name))
                      .exists())
                .list();

            // single result
            Cat c = select(cat).from(cat).where(cat.id.eq(1)).firstResult();

            //intermediate result storing
            JPATestQuery<Cat> select = query.select(cat);
            if (cat == other) {
                select.where(cat.ne(other));
            } else {
                select.where(cat.eq(other));
            }
            long count = select.count();
            List<Cat> result = select.list();
        }

        private <T> JPATestQuery<T> select(Expression<T> e) {
            JPATestQuery<Void> query = null;
            return query.select(e);
        }
    }

    JPATestQuery<T> from(EntityPath<?> path);

    JPATestQuery<T> where(Predicate... where);

    <P> JPATestQuery<P> select(Expression<P> projection);

    JPATestQuery<Tuple> select(Expression<?>... projection);

    // SubQueryExpression

    Predicate exists();

    Predicate notExists();

    // SimpleProjectable

//    boolean exists();

//    boolean notExists();

    CloseableIterator<T> iterate();

    List<T> list();

    @Nullable
    T firstResult();

    @Nullable
    T uniqueResult();

    SearchResults<T> listResults();

    long count();
}
