package org.our_place.gallery.application.usecase.command;

import java.util.UUID;

public record DeleteMediaCommand(UUID mediaId, UUID requestingUserId) {
}