package org.our_place.gallery.application.usecase.command;

import java.util.UUID;

public record RemoveMediaReactionCommand(UUID mediaId, UUID userLoginId) {
}