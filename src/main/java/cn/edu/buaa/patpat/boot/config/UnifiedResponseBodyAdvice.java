package cn.edu.buaa.patpat.boot.config;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class UnifiedResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> type = ResolvableType.forMethodParameter(returnType).resolve();
        return type == MessageResponse.class || type == DataResponse.class;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        int status = HttpStatus.OK.value();

        if (body instanceof MessageResponse message) {
            status = message.getStatus();
        } else if (body instanceof DataResponse<?> value) {
            status = value.getStatus();
        }

        if (status != HttpStatus.OK.value()) {
            response.setStatusCode(HttpStatus.valueOf(status));
            logRequest(request);
        }

        return body;
    }

    private void logRequest(ServerHttpRequest request) {
        String body;
        try {
            body = request.getBody().toString();
        } catch (IOException e) {
            body = "Failed to read request body";
        }
        log.warn("FAILED: {} {} - {}", request.getMethod(), request.getURI(), body);
    }
}
