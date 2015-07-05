package com.querydsl.example.config;

import com.querydsl.example.dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;

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
