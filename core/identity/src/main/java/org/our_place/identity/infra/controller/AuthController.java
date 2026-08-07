package org.our_place.identity.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.identity.infra.controller.request.RefreshTokenRequest;
import org.our_place.identity.infra.controller.response.TokenResponse;
import org.our_place.identity.infra.provider.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        Claims claims = jwtProvider.parseToken(request.refreshToken());

        if (claims == null || !"REFRESH".equals(claims.get("type", String.class))) {
            return ResponseEntity.status(401).build();
        }

        UUID userId = UUID.fromString(claims.getSubject());
        String email = claims.get("email", String.class);

        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        if (roles == null) roles = List.of();

        String newAccessToken = jwtProvider.generateAccessToken(userId, email, roles);

        return ResponseEntity.ok(new TokenResponse(newAccessToken));
    }
}