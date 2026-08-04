package org.our_place.identity.domain.exception;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;

public class EmailAlreadyRegisteredException extends ResultException {

    public EmailAlreadyRegisteredException(String email) {
        super("Email " + email + " already registered",
                new ResultIssue("EMAIL_ALREADY_REGISTERED",
                        "El email ya está registrado",
                        ResultIssue.Severity.WARNING));
    }

}
