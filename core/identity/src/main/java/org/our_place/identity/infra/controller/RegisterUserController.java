package org.our_place.identity.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.usecase.RegisterUserUseCase;
import org.our_place.identity.application.usecase.command.RegisterUserCommand;
import org.our_place.identity.application.usecase.output.RegisterUserOutput;
import org.our_place.identity.infra.controller.request.RegisterUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RegisterUserController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserOutput> register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserOutput output = registerUserUseCase.execute(new RegisterUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}