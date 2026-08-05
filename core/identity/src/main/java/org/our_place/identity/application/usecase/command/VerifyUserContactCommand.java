package org.our_place.identity.application.usecase.command;

import java.util.UUID;

public record VerifyUserContactCommand(UUID userLoginId, String contactTypeCode, String value) {}