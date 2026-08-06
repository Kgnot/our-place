package org.our_place.identity.application.service.dto;

import java.util.UUID;

public record UserLoginDto(
        UUID uuid,
        String email,
        String firstName
) {
}
