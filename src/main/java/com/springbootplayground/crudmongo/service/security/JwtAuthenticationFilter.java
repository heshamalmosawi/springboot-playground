package com.springbootplayground.crudmongo.service.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final jwtService jwtService;

    public JwtAuthenticationFilter(jwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Filters incoming HTTP requests to validate JWT tokens from the
     * "Authorization" header. If the header is missing or does not start with
     * "Bearer ", the request is passed along the filter chain. If the token is
     * invalid or expired, an HTTP 401 Unauthorized response is returned.
     * Otherwise, the request proceeds through the filter chain.
     *
     * @param request the HTTP request to be processed
     * @param response the HTTP response to be sent
     * @param filterChain the filter chain to pass the request and response to
     * the next filter
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        System.err.println("JwtAuthenticationFilter: Processing request...");
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("JwtAuthenticationFilter: Found Authorization header, validating token...");

        String token = authHeader.substring(7);
        try {

            // Extract claims (like email and role) from the token
            var claims = jwtService.extractAllClaims(token);
            String email = claims.getSubject();
            String id = (String) claims.get("id");
            String role = (String) claims.get("role");
            System.out.println("JwtAuthenticationFilter: Token claims extracted - Email: " + email + ", Role: " + role + ", ID: " + id);
            if (id != null && email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Create authorities list
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                System.out.println("JwtAuthenticationFilter: Authorities created for user: " + email + " with role: " + role);
                // Set the Authentication in SecurityContext
                var userDetails = new org.springframework.security.core.userdetails.User(id, "", authorities);
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("\n\n\n\nInvalid or expired token: " + e.getMessage());
            response.getWriter().write("Invalid or expired token");
            return;
        }
        System.err.println("JwtAuthenticationFilter: Token is valid, proceeding with request...");
        filterChain.doFilter(request, response);
    }

}
