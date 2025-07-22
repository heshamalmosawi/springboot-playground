// package com.springbootplayground.security;

// import io.jsonwebtoken.*;

// import org.springframework.stereotype.Component;

// import java.util.Date;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.security.Keys;
// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.SignatureException;
// import java.security.Key;


// import org.springframework.beans.factory.annotation.Value;


// @Component
// public class jwtUtil {
//     @Value("${jwt.secret}")
//     private String jwtSecret;
//     private final long jwtExpirationMs = 86400000; // 1 day

//     public String generateToken(String userId, String role) {
//         return Jwts.builder()
//                 .setSubject(userId)
//                 .claim("role", role)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                 .signWith(SignatureAlgorithm.HS256, jwtSecret)
//                 .compact();
//     }

//     public String getUserIdFromToken(String token) {
//         return parseClaims(token).getSubject();
//     }

//     public String getRoleFromToken(String token) {
//         return (String) parseClaims(token).get("role");
//     }

//     public boolean validateToken(String token) {
//         try {
//             parseClaims(token);
//             return true;
//         } catch (JwtException | IllegalArgumentException e) {
//             return false;
//         }
//     }

//     private Claims parseClaims(String token) {
//         return Jwts.parserBuilder()
//                     .setSigningKey(jwtSecret)
//                     .build()
//                     .parseClaimsJws(token)
//                     .getBody();
//     }
// }
