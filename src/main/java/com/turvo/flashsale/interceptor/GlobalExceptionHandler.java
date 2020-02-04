package com.turvo.flashsale.interceptor;

import com.turvo.flashsale.dto.Response;
import com.turvo.flashsale.exception.IlligallFlashSaleAccess;
import com.turvo.flashsale.exception.OrderException;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Response> handleExceptions(Exception ex, WebRequest request) {
        log.error("Error : ", ex);
        String exceptionMessage = defaultString(ex.getMessage(), ExceptionUtils.getRootCause(ex).toString());
        Response response = Response.buildError(new Error(exceptionMessage));
        printJson(response);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler({IlligallFlashSaleAccess.class})
    public final ResponseEntity<Response> handleExceptions(IlligallFlashSaleAccess ex, WebRequest request) {
        log.error("Error : ", ex);
        String exceptionMessage = defaultString(ex.getMessage(), ExceptionUtils.getRootCause(ex).toString());
        Response response = Response.buildError(new Error(exceptionMessage));
        printJson(response);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler({OrderException.class})
    public final ResponseEntity<Response> handleExceptions(OrderException ex, WebRequest request) {
        log.error("Error : ", ex);
        String exceptionMessage = defaultString(ex.getMessage(), ExceptionUtils.getRootCause(ex).toString());
        Response response = Response.buildError(new Error(exceptionMessage));
        printJson(response);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    private void printJson(Response response) {
        log.info(ReflectionToStringBuilder.toString(response, new MultilineRecursiveToStringStyle()));
    }
}
