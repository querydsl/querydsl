package com.querydsl.example.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.querydsl.example.dao.CustomerDao;
import com.querydsl.example.dao.CustomerDaoImpl;
import com.querydsl.example.dao.OrderDao;
import com.querydsl.example.dao.OrderDaoImpl;
import com.querydsl.example.dao.PersonDao;
import com.querydsl.example.dao.PersonDaoImpl;
import com.querydsl.example.dao.ProductDao;
import com.querydsl.example.dao.ProductDaoImpl;
import com.querydsl.example.dao.SupplierDao;
import com.querydsl.example.dao.SupplierDaoImpl;

@Configuration
@EnableTransactionManagement
@Import(JdbcConfiguration.class)
public class AppConfiguration {

   @Inject Environment env;

   @Bean
   public CustomerDao customerDao() {
       return new CustomerDaoImpl();
   }

   @Bean
   public OrderDao orderDao() {
       return new OrderDaoImpl();
   }

   @Bean
   public PersonDao personDao() {
       return new PersonDaoImpl();
   }

   @Bean
   public ProductDao productDao() {
       return new ProductDaoImpl();
   }

   @Bean
   public SupplierDao supplierDao() {
       return new SupplierDaoImpl();
   }

}
