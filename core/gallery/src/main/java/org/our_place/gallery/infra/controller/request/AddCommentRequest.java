package org.our_place.gallery.infra.controller.request;

import jakarta.validation.constraints.NotBlank;

public record AddCommentRequest(
        @NotBlank String content
) {
}