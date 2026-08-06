package org.our_place.identity.infra.controller.request;

import java.time.LocalDate;

public record UpdateUserProfileRequest(
        String firstName,
        String lastName,
        String avatarUrl,
        LocalDate birthDate,
        String timezone,
        String locale
) {}
