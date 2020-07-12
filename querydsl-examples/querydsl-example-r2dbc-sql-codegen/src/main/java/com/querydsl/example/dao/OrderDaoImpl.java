package com.querydsl.example.dao;

import com.querydsl.core.dml.ReactiveStoreClause;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.CustomerPaymentMethod;
import com.querydsl.example.dto.Order;
import com.querydsl.example.dto.OrderProduct;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import com.querydsl.r2dbc.group.ReactiveGroupBy;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.r2dbc.QCustomerOrder.customerOrder;
import static com.querydsl.example.r2dbc.QCustomerOrderProduct.customerOrderProduct;
import static com.querydsl.example.r2dbc.QCustomerPaymentMethod.customerPaymentMethod;

@Transactional
public class OrderDaoImpl implements OrderDao {

    @Inject
    R2DBCQueryFactory queryFactory;

    final QBean<OrderProduct> orderProductBean = bean(OrderProduct.class,
            customerOrderProduct.productId, customerOrderProduct.comments, customerOrderProduct.quantity);

    final QBean<Order> orderBean = bean(Order.class,
            customerOrder.id, customerOrder.orderPlacedDate, customerOrder.orderPaidDate,
            customerOrder.orderStatus, customerOrder.totalOrderPrice,
            bean(CustomerPaymentMethod.class, customerPaymentMethod.all()).as("customerPaymentMethod"),
            GroupBy.set(orderProductBean).as("orderProducts"));

    @Override
    public Mono<Order> findById(long id) {
        return getBaseQuery(customerOrder.id.eq(id)).singleOrEmpty();
    }

    @Override
    public Flux<Order> findAll(Predicate... where) {
        return getBaseQuery(where);
    }

    private <T extends ReactiveStoreClause<T>> T populate(T dml, Order o) {
        return dml
                .set(customerOrder.customerPaymentMethodId, o.getCustomerPaymentMethod().getId())
                .set(customerOrder.orderPlacedDate, o.getOrderPlacedDate())
                .set(customerOrder.totalOrderPrice, o.getTotalOrderPrice());
    }

    @Override
    public Mono<Order> save(Order o) {
        Long id = o.getId();

        if (id == null) {
            return insert(o);
        }

        return update(o);
    }

    public Mono<Order> insert(Order o) {
        return populate(queryFactory.insert(customerOrder), o)
                .executeWithKey(customerOrder.id)
                .flatMap(id -> {
                    R2DBCInsertClause insert = queryFactory.insert(customerOrderProduct);
                    for (OrderProduct op : o.getOrderProducts()) {
                        insert
                                .set(customerOrderProduct.orderId, id)
                                .set(customerOrderProduct.comments, op.getComments())
                                .set(customerOrderProduct.productId, op.getProductId())
                                .set(customerOrderProduct.quantity, op.getQuantity())
                                .addBatch();
                    }
                    return insert
                            .execute()
                            .map(__ -> {
                                o.setId(id);

                                return o;
                            });
                });
    }

    public Mono<Order> update(Order o) {
        Long id = o.getId();

        return populate(queryFactory.update(customerOrder), o)
                .where(customerOrder.id.eq(id))
                .execute()
                .flatMap(__ -> {
                    // delete orderproduct rows
                    return queryFactory
                            .delete(customerOrderProduct)
                            .where(customerOrderProduct.orderId.eq(id))
                            .execute()
                            .flatMap(___ -> {
                                R2DBCInsertClause insert = queryFactory.insert(customerOrderProduct);
                                for (OrderProduct op : o.getOrderProducts()) {
                                    insert
                                            .set(customerOrderProduct.orderId, id)
                                            .set(customerOrderProduct.comments, op.getComments())
                                            .set(customerOrderProduct.productId, op.getProductId())
                                            .set(customerOrderProduct.quantity, op.getQuantity())
                                            .addBatch();
                                }
                                return insert
                                        .execute()
                                        .map(____ -> {
                                            o.setId(id);

                                            return o;
                                        });
                            });
                });
    }

    @Override
    public Mono<Long> count() {
        return queryFactory.from(customerOrder).fetchCount();
    }

    @Override
    public Mono<Void> delete(Order o) {
        // TODO use combined delete clause
        return queryFactory
                .delete(customerOrderProduct)
                .where(customerOrderProduct.orderId.eq(o.getId()))
                .execute()
                .then(queryFactory
                        .delete(customerOrder)
                        .where(customerOrder.id.eq(o.getId()))
                        .execute()
                )
                .then();
    }

    private Flux<Order> getBaseQuery(Predicate... where) {
        return (Flux<Order>) queryFactory
                .select(orderBean)
                .from(customerOrder)
                .leftJoin(customerOrder.paymentMethodFk, customerPaymentMethod)
                .leftJoin(customerOrder._orderFk, customerOrderProduct)
                .where(where)
                .transform(ReactiveGroupBy.groupBy(customerOrder.id).flux(orderBean));

    }

}
