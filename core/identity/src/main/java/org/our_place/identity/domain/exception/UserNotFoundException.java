package org.our_place.identity.domain.exception;


// mirar si lo hacemos un error de aplicacion, no de dominio

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class UserNotFoundException extends ResultException {

    public UserNotFoundException() {
        super(
                "Usuario no encontrado",
                new ResultIssue("USER_NOT_FOUND",
                        "El usuario no existe",
                        ResultIssue.Severity.WARNING)
        );
    }
}
