package org.our_place.identity.application.service.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GetMeUserDto(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        String avatarUrl,
        LocalDate birthDate,
        String timezone,
        String locale,
        String statusCode,
        boolean mfaEnabled,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
