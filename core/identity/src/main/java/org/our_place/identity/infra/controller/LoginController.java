package org.our_place.identity.infra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.usecase.LoginUseCase;
import org.our_place.identity.application.usecase.command.LoginCommand;
import org.our_place.identity.application.usecase.output.LoginOutput;
import org.our_place.identity.infra.controller.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginOutput> login(@Valid @RequestBody LoginRequest request) {
        LoginOutput output = loginUseCase.execute(new LoginCommand(
                request.email(),
                request.password()
        ));
        return ResponseEntity.ok(output);
    }
}