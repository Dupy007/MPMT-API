package com.dupy.MPMT.security;


import com.dupy.MPMT.model.User;
import com.dupy.MPMT.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(2)
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String username = (String) request.getAttribute("username");
        if (request.getMethod() == "PUT") {
            User user = userService.findByUsername(username);
        }
        filterChain.doFilter(request, response);
    }
}