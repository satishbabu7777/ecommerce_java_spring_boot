package com.ecommerce.apigateway.util;

import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey12345";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    public void validateToken(String token) {

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}