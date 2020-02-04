package com.turvo.flashsale.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class Error {

    public String errorCode = "APPLICATION_ERROR";
    public String errorLevel = "ERROR";
    public String message;

    public Error(String message) {
        this.message = message;
    }
}
