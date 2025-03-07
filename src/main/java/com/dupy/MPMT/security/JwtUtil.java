package com.dupy.MPMT.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète pour signer le token (à garder en sécurité)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Générer un token pour un nom d'utilisateur
    public String generateToken(String username) {
        Date now = new Date();
        // Durée de validité du token (exemple : 1 heures)
        long jwtExpirationInMs = 60 * 60 * 1000;
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiryDate)
                .signWith(key).compact();
    }

    // Extraire le nom d'utilisateur depuis le token
    public String getUsernameFromJWT(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Valider le token
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            // Token invalide ou expiré
            System.out.println("JWT validation error: " + ex.getMessage());
        }
        return false;
    }

}