package com.cab.user.config;

import com.cab.user.auth.service.CustomUserDetailsService;
import com.cab.user.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String token = extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (token != null) {

            try {
                if (jwtService.isRefreshToken(token)) {
                    if (!request.getRequestURI().equals("/refresh-access-token")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Refresh token cannot be used for API access");
                        return;
                    }
                } else {
                    final String username = jwtService.extractUsername(token);
                    processToken(username, token, request);
                }
            } catch (Exception e) {
                logger.error("Error processing token", e);
            }

        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(final String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            logger.info("Invalid or missing Authorization header.");
            return null;
        }
    }

    private void processToken(final String username, final String token, final HttpServletRequest request) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }
}