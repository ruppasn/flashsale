package com.turvo.flashsale.endpoint;


import com.turvo.flashsale.dto.CustomerDTO;
import com.turvo.flashsale.model.Customer;
import com.turvo.flashsale.service.customer.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.turvo.flashsale.builder.CustomerBuilder.buildCustomer;

@RestController
@RequestMapping("v1/customers")
public class CustomerRegistrationController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ICustomerService customerService;

    @PostMapping
    public Customer registerCustomer(@RequestBody CustomerDTO customer) {
        log.info("customer registered successfully");
        return customerService.registerCustomer(buildCustomer(customer));
    }
}
