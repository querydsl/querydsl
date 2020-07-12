package com.querydsl.example.dao;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.example.dto.Address;
import com.querydsl.example.dto.Customer;
import com.querydsl.example.dto.CustomerAddress;
import com.querydsl.example.dto.Person;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import com.querydsl.r2dbc.group.ReactiveGroupBy;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.example.r2dbc.QAddress.address;
import static com.querydsl.example.r2dbc.QCustomer.customer;
import static com.querydsl.example.r2dbc.QCustomerAddress.customerAddress;
import static com.querydsl.example.r2dbc.QPerson.person;

@Transactional
public class CustomerDaoImpl implements CustomerDao {

    @Inject
    R2DBCQueryFactory queryFactory;

    final QBean<CustomerAddress> customerAddressBean = bean(CustomerAddress.class,
            customerAddress.addressTypeCode, customerAddress.fromDate, customerAddress.toDate,
            bean(Address.class, address.all()).as("address"));

    final QBean<Customer> customerBean = bean(Customer.class,
            customer.id, customer.name,
            bean(Person.class, person.all()).as("contactPerson"),
            GroupBy.set(customerAddressBean).as("addresses"));

    @Override
    public Mono<Customer> findById(long id) {
        return getBaseQuery(customer.id.eq(id)).singleOrEmpty();
    }

    @Override
    public Flux<Customer> findAll(Predicate... where) {
        return getBaseQuery(where);
    }

    @Override
    public Mono<Customer> save(Customer c) {
        Long id = c.getId();

        if (id == null) {
            return insert(c);
        }

        return update(c);
    }

    public Mono<Customer> insert(Customer c) {
        return queryFactory
                .insert(customer)
                .set(customer.name, c.getName())
                .set(customer.contactPersonId, c.getContactPerson().getId())
                .executeWithKey(customer.id)
                .flatMap(id -> {
                    c.setId(id);

                    R2DBCInsertClause insert = queryFactory.insert(customerAddress);
                    return Flux
                            .fromIterable(c.getAddresses())
                            .flatMap(ca -> {
                                if (ca.getAddress().getId() == null) {
                                    return queryFactory
                                            .insert(address)
                                            .populate(ca.getAddress())
                                            .executeWithKey(address.id)
                                            .map(caId -> {
                                                ca.getAddress().setId(caId);

                                                return ca;
                                            });
                                }

                                return Mono.just(ca);
                            })
                            .map(ca -> insert
                                    .set(customerAddress.customerId, id)
                                    .set(customerAddress.addressId, ca.getAddress().getId())
                                    .set(customerAddress.addressTypeCode, ca.getAddressTypeCode())
                                    .set(customerAddress.fromDate, ca.getFromDate())
                                    .set(customerAddress.toDate, ca.getToDate())
                                    .addBatch()
                            )
                            .collectList()
                            .flatMap(__ -> insert.execute())
                            .map(__ -> c);
                });
    }

    public Mono<Customer> update(Customer c) {
        return queryFactory
                .update(customer)
                .set(customer.name, c.getName())
                .set(customer.contactPersonId, c.getContactPerson().getId())
                .where(customer.id.eq(c.getId()))
                .execute()
                .flatMap(id -> {
                    c.setId(id);

                    // delete address rows
                    return queryFactory
                            .delete(customerAddress)
                            .where(customerAddress.customerId.eq(id))
                            .execute()
                            .flatMap(__ -> {
                                R2DBCInsertClause insert = queryFactory.insert(customerAddress);
                                return Flux
                                        .fromIterable(c.getAddresses())
                                        .flatMap(ca -> {
                                            if (ca.getAddress().getId() == null) {
                                                return queryFactory
                                                        .insert(address)
                                                        .populate(ca.getAddress())
                                                        .executeWithKey(address.id)
                                                        .map(caId -> {
                                                            ca.getAddress().setId(caId);

                                                            return ca;
                                                        });
                                            }

                                            return Mono
                                                    .just(ca.getAddress().getId())
                                                    .map(caId -> {
                                                        ca.getAddress().setId(caId);

                                                        return ca;
                                                    });
                                        })
                                        .map(ca -> insert.set(customerAddress.customerId, id)
                                                .set(customerAddress.addressId, ca.getAddress().getId())
                                                .set(customerAddress.addressTypeCode, ca.getAddressTypeCode())
                                                .set(customerAddress.fromDate, ca.getFromDate())
                                                .set(customerAddress.toDate, ca.getToDate())
                                                .addBatch()
                                        )
                                        .collectList()
                                        .flatMap(___ -> insert.execute())
                                        .map(___ -> c);
                            });
                });
    }

    @Override
    public Mono<Long> count() {
        return queryFactory.from(customer).fetchCount();
    }

    @Override
    public Mono<Void> delete(Customer c) {
        // TODO use combined delete clause
        return queryFactory
                .delete(customerAddress)
                .where(customerAddress.customerId.eq(c.getId()))
                .execute()
                .then(queryFactory
                        .delete(customer)
                        .where(customer.id.eq(c.getId()))
                        .execute())
                .then();
    }

    private Flux<Customer> getBaseQuery(Predicate... where) {
        return (Flux<Customer>) queryFactory
                .from(customer)
                .leftJoin(customer.contactPersonFk, person)
                .leftJoin(customer._customer3Fk, customerAddress)
                .leftJoin(customerAddress.addressFk, address)
                .where(where)
                .transform(ReactiveGroupBy.groupBy(customer.id).flux(customerBean));
    }

}
