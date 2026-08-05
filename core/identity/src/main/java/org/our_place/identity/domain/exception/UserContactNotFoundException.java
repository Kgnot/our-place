package org.our_place.identity.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

// mirar si lo hacemos un error de aplicacion, no de dominio
public class UserContactNotFoundException extends ResultException {

    public UserContactNotFoundException() {
        super(
                "Contacto de usuario no encontrado",
                new ResultIssue("USER_CONTACT_NOT_FOUND",
                        "El contacto de usuario no existe",
                        ResultIssue.Severity.WARNING)
        );
    }
}