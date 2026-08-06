package org.our_place.identity.application.usecase.command;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserProfileCommand(
        UUID userId,
        String firstName,
        String lastName,
        String avatarUrl,
        LocalDate birthDate,
        String timezone,
        String locale
) {}
