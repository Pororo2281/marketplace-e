package org.example.userservice.Security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.userservice.Enums.UserStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value(("${jwt.refresh.secret}"))
    private String jwtRefreshToken;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpMs;

    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

       return Jwts.builder()
               .setSubject(userPrincipal.getId().toString())
               .claim("type","ACCESS")
               .claim("role",userPrincipal.getRole())
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
               .signWith(key(jwtSecret),SignatureAlgorithm.HS256)
               .compact();
    }

    public String generateTokenFromId(Long id, String role, UserStatus status) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("type","ACCESS")
                .claim("role",role)
                .claim("status",status.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key(jwtSecret),SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .claim("type","REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+jwtRefreshExpMs))
                .signWith(key(jwtRefreshToken),SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshTokenFromId(Long id) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("type","REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpMs))
                .signWith(key(jwtRefreshToken),SignatureAlgorithm.HS256)
                .compact();
    }



    public String getIdFromJWTToken(String token, String tokenType) {
        String secret = "ACCESS".equals(tokenType) ? jwtSecret : jwtRefreshToken;
        return Jwts.parserBuilder()
                .setSigningKey(key(secret))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key(jwtSecret))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    public boolean validateRefreshToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key(jwtRefreshToken))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }



    private Key key(String secret){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

}
