package com.turvo.flashsale.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.turvo.flashsale.dto.CustomerDTO;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@Table(name = "ADDRESS")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ADDRESS_ID", columnDefinition = "serial")
    private Integer id;

    @Column(name = "DOOR_NO")
    private String doorNo;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "STATE")
    private String state;

    @Column(name = "PIN")
    private Integer pin;

    @Transient
    public static Address buildAddress(CustomerDTO customerDTO) {
        Address address = new Address();
        address.district = customerDTO.getDistrict();
        address.doorNo = customerDTO.getDoorNo();
        address.state = customerDTO.getState();
        address.pin = customerDTO.getPin();
        return address;
    }
}
