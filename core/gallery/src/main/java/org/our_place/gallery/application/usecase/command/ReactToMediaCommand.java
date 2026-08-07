package org.our_place.gallery.application.usecase.command;

import java.util.UUID;

public record ReactToMediaCommand(UUID mediaId, UUID userLoginId, String reactionType) {
}