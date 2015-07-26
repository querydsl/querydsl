package com.querydsl.example.dao;

import static com.mysema.query.types.Projections.bean;
import static com.querydsl.example.sql.QAddress.address;
import static com.querydsl.example.sql.QCustomer.customer;
import static com.querydsl.example.sql.QCustomerAddress.customerAddress;
import static com.querydsl.example.sql.QPerson.person;

import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.group.GroupBy;
import com.mysema.query.sql.SQLQueryFactory;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QBean;
import com.querydsl.example.dto.Address;
import com.querydsl.example.dto.Customer;
import com.querydsl.example.dto.CustomerAddress;
import com.querydsl.example.dto.Person;

@Transactional
public class CustomerDaoImpl implements CustomerDao {

    @Inject
    SQLQueryFactory queryFactory;

    final QBean<CustomerAddress> customerAddressBean = bean(CustomerAddress.class,
            customerAddress.addressTypeCode, customerAddress.dateFrom, customerAddress.dateTo,
            bean(Address.class, address.all()).as("address"));

    final QBean<Customer> customerBean = bean(Customer.class,
            customer.id, customer.name,
            bean(Person.class, person.all()).as("contactPerson"),
            GroupBy.set(customerAddressBean).as("addresses"));

    @Override
    public Customer findById(long id) {
        List<Customer> customers = findAll(customer.id.eq(id));
        return customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public List<Customer> findAll(Predicate where) {
        return queryFactory.from(customer)
            .leftJoin(customer.contactPersonFk, person)
            .leftJoin(customer._customer3Fk, customerAddress)
            .leftJoin(customerAddress.addressFk, address)
            .where(where)
            .transform(GroupBy.groupBy(customer.id).list(customerBean));
    }

    @Override
    public Customer save(Customer c) {
        Long id = c.getId();

        if (id == null) {
            id = queryFactory.insert(customer)
                    .set(customer.name, c.getName())
                    .set(customer.contactPersonId, c.getContactPerson().getId())
                    .executeWithKey(customer.id);
            c.setId(id);
        } else {
            queryFactory.update(customer)
                .set(customer.name, c.getName())
                .set(customer.contactPersonId, c.getContactPerson().getId())
                .where(customer.id.eq(c.getId()))
                .execute();

            // delete address rows
            queryFactory.delete(customerAddress)
                .where(customerAddress.customerId.eq(id))
                .execute();
        }

        SQLInsertClause insert = queryFactory.insert(customerAddress);
        for (CustomerAddress ca : c.getAddresses()) {
            if (ca.getAddress().getId() == null) {
                ca.getAddress().setId(queryFactory.insert(address)
                    .populate(ca.getAddress())
                    .executeWithKey(address.id));
            }
            insert.set(customerAddress.customerId, id)
                .set(customerAddress.addressId, ca.getAddress().getId())
                .set(customerAddress.addressTypeCode, ca.getAddressTypeCode())
                .set(customerAddress.dateFrom, ca.getDateFrom())
                .set(customerAddress.dateTo, ca.getDateTo())
                .addBatch();
        }
        insert.execute();

        c.setId(id);
        return c;
    }

    @Override
    public long count() {
        return queryFactory.from(customer).count();
    }

    @Override
    public void delete(Customer c) {
        // TODO use combined delete clause
        queryFactory.delete(customerAddress)
            .where(customerAddress.customerId.eq(c.getId()))
            .execute();

        queryFactory.delete(customer)
            .where(customer.id.eq(c.getId()))
            .execute();
    }

}
