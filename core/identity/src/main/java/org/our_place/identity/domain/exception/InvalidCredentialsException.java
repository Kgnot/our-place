package org.our_place.identity.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class InvalidCredentialsException extends ResultException {

    public InvalidCredentialsException() {
        super(
                "Credenciales inválidas",
                new ResultIssue("INVALID_CREDENTIALS",
                        "El email o la contraseña son incorrectos",
                        ResultIssue.Severity.WARNING)
        );
    }
}