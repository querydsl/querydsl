package com.mysema.query.binding;

import org.junit.Test;

import com.mysema.query.binding.domain.Address;
import com.mysema.query.binding.domain.QCustomer;
import com.mysema.query.binding.domain.QCustomerDTO;


public class BindingsTest {
    
    private Bindings<QCustomer, QCustomerDTO> bindings;
    
    private Converter<Address,Long> addressConverter = new Converter<Address,Long>(){
        @Override
        public Long toTarget(Address a) {
            return a.getId();
        }
        @Override
        public Address toSource(Long id) {
            // should be DAO based Address access
            return new Address(id);
        }            
    };
    
    @Test
    public void bidirectional(){        
        bindings = new Bindings<QCustomer,QCustomerDTO>(){
            @Override
            public void configure(QCustomer entity, QCustomerDTO dto) {
                // compact
                bind(entity.firstName, dto.firstName);
                bind(entity.lastName, dto.lastName);
                bind(entity.address, dto.addressId, addressConverter);
            }            
        };        
    }
    
    @Test
    public void unidirectional(){        
        bindings = new Bindings<QCustomer,QCustomerDTO>(){
            @Override
            public void configure(QCustomer entity, QCustomerDTO dto) {
                // ->
                bind(entity.firstName).to(dto.firstName);
                bind(entity.lastName).to(dto.lastName);
                bind(entity.address).to(dto.addressId, addressConverter);
                
                // <-
                bind(entity.firstName).from(dto.firstName);
                bind(entity.lastName).from(dto.lastName);
//                bind(entity.address).from(dto.addressId, addressConverter);
                
            }            
        };    
    }

}
