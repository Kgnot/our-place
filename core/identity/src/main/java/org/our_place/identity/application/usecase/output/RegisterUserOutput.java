package org.our_place.identity.application.usecase.output;

import java.util.UUID;

public record RegisterUserOutput(
        UUID userId,
        String email,
        String status
) {
}
