package org.our_place.identity.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtProvider {

    private final SecretKey key;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtProvider(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration-ms}") long accessExpirationMs,
            @Value("${app.security.jwt.refresh-expiration-ms}") long refreshExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }


    public String generateAccessToken(UUID userId, UUID tenantId, String email, List<String> roles) {
        return buildToken(userId, tenantId, email, roles, accessExpirationMs, "ACCESS");
    }

    public String generateRefreshToken(UUID userId, UUID tenantId) {
        // El refresh token no necesita roles o email, solo lo mínimo para identificar
        return buildToken(userId, tenantId, null, null, refreshExpirationMs, "REFRESH");
    }

    private String buildToken(UUID userId, UUID tenantId, String email, List<String> roles, long expirationMs, String type) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        var builder = Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("roles", roles)
                .claim("type", type) // Necesario para diferenciarlos entre access y refresh
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key);

        if (email != null) builder.claim("email", email);
        if (roles != null) builder.claim("roles", roles);

        return builder.compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            return null; // Token inválido o expirado
        }
    }
}
