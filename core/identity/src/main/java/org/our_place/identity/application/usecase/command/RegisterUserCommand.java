package org.our_place.identity.application.usecase.command;

public record RegisterUserCommand(
        String email,
        String password,
        String firstName,
        String lastName
) {
}