package org.our_place.gallery.application.usecase.command;

import java.util.UUID;

public record UpdateMediaCaptionCommand(UUID mediaId, UUID requestingUserId, String caption) {
}