package org.our_place.gallery.application.usecase.command;

import java.util.UUID;

public record AddMediaCommentCommand(UUID mediaId, UUID userLoginId, String content) {
}