package com.dupy.MPMT.security;

import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@AllArgsConstructor
@ControllerAdvice
public class JwtResponseInterceptor implements ResponseBodyAdvice<Object> {

//    private JwtUtil jwtUtil;
//    private HttpServletRequest httpServletRequest;


    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
//        String username = (String) httpServletRequest.getAttribute("username");
//        String newToken = jwtUtil.generateToken(username);
//        response.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
//        httpServletRequest.removeAttribute("username");
        return body;
    }
}
