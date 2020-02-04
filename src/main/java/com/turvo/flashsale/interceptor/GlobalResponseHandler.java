package com.turvo.flashsale.interceptor;

import com.turvo.flashsale.dto.Response;
import com.turvo.flashsale.endpoint.CatalogController;
import com.turvo.flashsale.endpoint.CustomerRegistrationController;
import com.turvo.flashsale.endpoint.FlashSaleController;
import com.turvo.flashsale.endpoint.OrderController;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final List<String> responseAdviceControllers = Arrays.asList(
            CatalogController.class.getName(),
            CustomerRegistrationController.class.getName(),
            FlashSaleController.class.getName(),
            OrderController.class.getName()
    );

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return responseAdviceControllers.contains(returnType.getDeclaringClass().getName());
    }

    @Override
    public Response beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                    Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String json = ReflectionToStringBuilder.toString(nonNull(body) ? body : EMPTY, new MultilineRecursiveToStringStyle());
        log.info("Response : {}", json);
        return Response.buildSuccess(body);
    }
}
