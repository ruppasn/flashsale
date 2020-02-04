package com.turvo.flashsale.builder;

import com.turvo.flashsale.dto.CustomerDTO;
import com.turvo.flashsale.model.Customer;

import static com.turvo.flashsale.model.Address.buildAddress;

public class CustomerBuilder {

    public static Customer buildCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setEligibility(true);
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNO(customerDTO.getPhoneNO());
        customer.setName(customerDTO.getName());
        customer.setAddress(buildAddress(customerDTO));
        return customer;
    }

}
