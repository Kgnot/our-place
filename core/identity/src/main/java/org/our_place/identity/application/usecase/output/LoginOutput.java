package org.our_place.identity.application.usecase.output;

import java.util.UUID;

public record LoginOutput(UUID userId, String accessToken, String refreshToken) {
}
