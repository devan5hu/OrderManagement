package com.example.ordermanagement.auth;

import com.example.ordermanagement.exceptionHandler.InvalidTokenException;
import com.example.ordermanagement.security.CustomerDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static com.example.ordermanagement.auth.JwtKeyUtil.getKeyFromString;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String privateKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getPrivateKey(String key) {
        return getKeyFromString(key);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getPrivateKey(privateKey)).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(CustomerDetails customerDetails) {
        return Jwts.builder()
                .setSubject(customerDetails.getUsername())
                .claim("customerId", customerDetails.getCustomerId())
                .setIssuedAt(new Date())  // Set the issue date of the token
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getPrivateKey(privateKey) , SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     *  Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);

            // Check if token has expired
            if (claims.getExpiration().before(new Date())) {
                throw new InvalidTokenException("Token has expired.");
            }

            return true;
        } catch (MalformedJwtException e) {
            // Handle malformed token error
            throw new InvalidTokenException("Malformed JWT: The token structure is invalid.");
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token has expired.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("Unsupported JWT.");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid JWT token.");
        }
    }

    /**
     * Get username from token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
}
