package com.example.ordermanagement.auth;

import com.example.ordermanagement.exceptionHandler.ErrorResponse;
import com.example.ordermanagement.exceptionHandler.InvalidTokenException;
import com.example.ordermanagement.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.ordermanagement.constants.ErrorCodes.INVALID_TOKEN;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomerService customerService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromHeader(request);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            String origin = request.getHeader("Origin");
            if (origin == null) {
                origin = "*";
            }
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
            filterChain.doFilter(request, response);
        }catch (InvalidTokenException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden for invalid token
            ErrorResponse responseObj = new ErrorResponse("Invalid or Expired Token", INVALID_TOKEN);
            String json = new ObjectMapper().writeValueAsString(responseObj);
            response.getWriter().write(json);
            response.getWriter().flush();
        }
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);

        if (username != null) {
            UserDetails userDetails = customerService.loadUserByUsername(username);  // Load user by username (using the UserDetailsService)

            // Create an Authentication object
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return null;
    }
}
