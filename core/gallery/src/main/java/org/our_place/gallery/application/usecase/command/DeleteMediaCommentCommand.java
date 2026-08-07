package org.our_place.gallery.application.usecase.command;

import java.util.UUID;

public record DeleteMediaCommentCommand(UUID commentId, UUID requestingUserId) {
}