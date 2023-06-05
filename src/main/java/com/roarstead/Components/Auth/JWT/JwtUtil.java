package com.roarstead.Components.Auth.JWT;

import com.roarstead.App;
import com.roarstead.Components.Configs.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtil {

    public static String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Config.JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(App.getCurrentApp().getConfig().getSecretKey(), App.getCurrentApp().getConfig().getSecretAlg())
                .compact();
    }

    public static String extractSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(App.getCurrentApp().getConfig().getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(App.getCurrentApp().getConfig().getSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
