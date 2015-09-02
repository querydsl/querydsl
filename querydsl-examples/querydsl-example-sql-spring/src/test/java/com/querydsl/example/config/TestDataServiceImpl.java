package com.querydsl.example.config;

import com.google.common.collect.ImmutableSet;
import com.querydsl.example.dao.*;
import com.querydsl.example.dto.*;
import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
public class TestDataServiceImpl implements TestDataService {

    @Resource CustomerDao customerDao;
    @Resource OrderDao orderDao;
    @Resource PersonDao personDao;
    @Resource ProductDao productDao;
    @Resource SupplierDao supplierDao;

    @Override
    public void addTestData() {
        // suppliers
        Supplier supplier = new Supplier();
        supplier.setCode("acme");
        supplier.setName("ACME");
        supplierDao.save(supplier);

        Supplier supplier2 = new Supplier();
        supplier2.setCode("bigs");
        supplier2.setName("BigS");
        supplierDao.save(supplier2);

        // products
        Product product = new Product();
        product.setName("Screwdriver");
        product.setPrice(12.0);
        product.setSupplier(supplier);

        ProductL10n l10nEn = new ProductL10n();
        l10nEn.setLang("en");
        l10nEn.setName("Screwdriver");

        ProductL10n l10nDe = new ProductL10n();
        l10nDe.setLang("de");
        l10nDe.setName("Schraubenzieher");

        product.setLocalizations(ImmutableSet.of(l10nEn, l10nDe));
        productDao.save(product);

        Product product2 = new Product();
        product2.setName("Hammer");
        product2.setPrice(5.0);
        product2.setSupplier(supplier2);

        l10nEn = new ProductL10n();
        l10nEn.setLang("en");
        l10nEn.setName("Hammer");

        product2.setLocalizations(ImmutableSet.of(l10nEn));
        productDao.save(product2);

        // persons
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setEmail("john.doe@aexample.com");
        personDao.save(person);

        Person person2 = new Person();
        person2.setFirstName("Mary");
        person2.setLastName("Blue");
        person2.setEmail("mary.blue@example.com");
        personDao.save(person2);

        // customers
        Address address = new Address();
        address.setStreet("Mainstreet 1");
        address.setZip("00100");
        address.setTown("Helsinki");
        address.setCountry("FI");

        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setAddress(address);
        customerAddress.setAddressTypeCode("office");
        customerAddress.setFromDate(new LocalDate());

        Customer customer = new Customer();
        customer.setAddresses(ImmutableSet.of(customerAddress));
        customer.setContactPerson(person);
        customer.setName("SmallS");
        customerDao.save(customer);

        Customer customer2 = new Customer();
        customer2.setAddresses(ImmutableSet.<CustomerAddress>of());
        customer2.setContactPerson(person);
        customer2.setName("MediumM");
        customerDao.save(customer2);

        // orders
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setComments("my comments");
        orderProduct.setProductId(product.getId());
        orderProduct.setQuantity(4);

        CustomerPaymentMethod paymentMethod = new CustomerPaymentMethod();
        paymentMethod.setCardNumber("11111111111");
        paymentMethod.setCustomerId(customer.getId());
        paymentMethod.setFromDate(new LocalDate());
        paymentMethod.setPaymentMethodCode("abc");

        Order order = new Order();
        order.setCustomerPaymentMethod(paymentMethod);
        order.setOrderPlacedDate(new LocalDate());
        order.setOrderProducts(ImmutableSet.of(orderProduct));
        order.setTotalOrderPrice(13124.00);
        orderDao.save(order);
    }

}
