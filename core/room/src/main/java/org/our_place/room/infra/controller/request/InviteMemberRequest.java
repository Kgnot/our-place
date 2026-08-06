package org.our_place.room.infra.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InviteMemberRequest(
        @NotBlank @Email String invitedEmail,
        @NotBlank String roleCode
) {
}