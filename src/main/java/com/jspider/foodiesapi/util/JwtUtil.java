package com.jspider.foodiesapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
// Import the Function interface
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    /**
     * Generates a new JWT token for a given UserDetails object.
     */
    public String generateToken(UserDetails userDetails) {
        // Corrected spelling of 'claims'
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Builds and signs the JWT token.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 10 hour expiration
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // --- Extraction Methods ---

    /**
     * Extracts a specific claim from the token using a ClaimsResolver function.
     */
    // Fixed generics syntax for Function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the token and retrieves all claims.
     */
    private Claims extractAllClaims(String token) {
        // Fixed Jwts.parser() chain syntax
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Helper method to extract the username (subject) from the token.
     */
    // Added implementation for extractUsername
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Helper method to extract the expiration date from the token.
     */
    // Added implementation for extractExpiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // --- Validation Methods ---

    /**
     * Checks if the token is expired.
     */
    private Boolean isTokenExpired(String token) {
        // Fixed method name to match the implementation added above
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the token against the user details.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        // Fixed method chain syntax (username extractUsername(token);)
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}