package org.our_place.identity.application.service.dto;

import java.util.UUID;

public record UserProfileDto(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        String statusCode
) {}