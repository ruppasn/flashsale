package com.turvo.flashsale.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class Response<T, V> {

    private int statusCode;
    private T data;
    private V error;
    private boolean success;

    public Response() {
    }

    private static <T, V> Response<T, V> build() {
        return new Response<T, V>();
    }

    public static <T, V> Response buildError(V v) {
        return build().buildError(v, INTERNAL_SERVER_ERROR.value());
    }

    private Response<T, V> buildError(V v, int code) {
        this.success = false;
        this.statusCode = code;
        this.error = v;
        return this;
    }

    public static <T, V> Response buildSuccess(T t) {
        return build().successOk().successData(t);
    }

    private Response<T, V> successData(T t) {
        this.data = t;
        return this;
    }

    public static Response buildSuccess() {
        return build().successOk();
    }

    private Response<T, V> successOk() {
        this.success = true;
        this.statusCode = OK.value();
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getData() {
        return data;
    }

    public V getError() {
        return error;
    }

    public boolean getSuccess() {
        return success;
    }
}
