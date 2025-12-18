package com.jspider.foodiesapi.jwtfilter;

import com.jspider.foodiesapi.util.JwtUtil; // Assuming this is where your JwtUtil lives
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor; // Used for constructor injection
import org.springframework.lang.NonNull; // To mark required parameters
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // Automatically creates a constructor for final fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Removed the illegal @Override annotation.
    // Made fields final for proper dependency injection (via constructor created by @RequiredArgsConstructor)
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil; // We need the utility class to validate and extract claims

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check for the Authorization header and "Bearer " prefix
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Exit the filter if no token is found
        }

        // 2. Extract JWT and Email
        jwt = authHeader.substring(7); // "Bearer " is 7 characters long
        userEmail = jwtUtil.extractUsername(jwt);

        // 3. Check if email is valid and user is not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 4. Validate the token
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // 5. Create Authentication object
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials are null in production, we use the token
                        userDetails.getAuthorities()
                );

                // Add request details
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6. Update Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}