package org.our_place.identity.application.usecase.output;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UpdateUserProfileOutput(
        UUID userId,
        String firstName,
        String lastName,
        String avatarUrl,
        LocalDate birthDate,
        String timezone,
        String locale,
        OffsetDateTime updatedAt
) {}
