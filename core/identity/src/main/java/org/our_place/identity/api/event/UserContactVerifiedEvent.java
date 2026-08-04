package org.our_place.identity.api.event;

import org.our_place.common.shared.SharedDomain;

import java.util.UUID;

@SharedDomain(description = "Event triggered when a user contact (email/phone) is verified")
public record UserContactVerifiedEvent(
        UUID userId,
        String contactTypeCode,
        String value
) {
}