package org.our_place.affection.controller.request;


import jakarta.validation.constraints.NotBlank;

public record CreateLoveNoteRequest(
        @NotBlank String typeCode,
        @NotBlank String content
) {}